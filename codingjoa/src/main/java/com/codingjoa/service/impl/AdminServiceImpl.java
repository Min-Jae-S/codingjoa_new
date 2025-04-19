package com.codingjoa.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminUserAddrDto;
import com.codingjoa.dto.AdminUserAuthDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.dto.AdminUserPasswordChangeDto;
import com.codingjoa.dto.AdminUserRegistrationDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.entity.AdminUser;
import com.codingjoa.entity.Auth;
import com.codingjoa.entity.User;
import com.codingjoa.error.ExpectedException;
import com.codingjoa.mapper.AdminMapper;
import com.codingjoa.mapper.AuthMapper;
import com.codingjoa.mapper.UserMapper;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.AdminUserCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.AdminService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class AdminServiceImpl implements AdminService {

	private final AdminMapper adminMapper;
	private final UserMapper userMapper;
	private final AuthMapper authMapper;
	private final PasswordEncoder passwordEncoder;
	private final int pageRange;

	public AdminServiceImpl(AdminMapper adminMapper, UserMapper userMapper, AuthMapper authMapper,
			PasswordEncoder passwordEncoder, @Value("${pagination.pageRange}") int pageRange) {
		this.adminMapper = adminMapper;
		this.userMapper = userMapper;
		this.authMapper = authMapper;
		this.passwordEncoder = passwordEncoder;
		this.pageRange = pageRange;
	}

	@Override
	public List<AdminUserDto> getPagedUsers(AdminUserCriteria adminUserCri) {
		log.info("\t > find pagedUsers");
		return adminMapper.findPagedUsers(adminUserCri)
				.stream()
				.map(adminUser -> AdminUserDto.from(adminUser))
				.collect(Collectors.toList());
	}
	
	@Override
	public Pagination getUserPagination(AdminUserCriteria adminUserCri) {
		int totalCnt = adminMapper.findTotalCntForUserPaging(adminUserCri);
		return (totalCnt > 0) ? new Pagination(totalCnt, adminUserCri.getPage(), adminUserCri.getRecordCnt(), pageRange) : null;
	}
	
	@Override
	public void updateNickname(NicknameDto nicknameDto, Long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.admin.userNotFound");
		}

		String currentNickname = user.getNickname();
		String newNickname = nicknameDto.getNickname();
		if (!newNickname.equals(currentNickname) && userMapper.isNicknameExist(newNickname)) {
			throw new ExpectedException("error.admin.nicknameExists", "nickname");
		}

		User modifyUser = User.builder()
				.id(user.getId())
				.nickname(newNickname)
				.build();

		boolean isUpdated = userMapper.updateNickname(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.admin.updateNickname");
		}
	}

	@Override
	public void updateEmail(EmailDto emailDto, Long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.admin.userNotFound");
		}

		String currentEmail = user.getEmail();
		String newEmail = emailDto.getEmail();
		if (!currentEmail.equals(newEmail) && userMapper.isEmailExist(newEmail)) {
			throw new ExpectedException("error.admin.emailExists", "email");
		}

		User modifyUser = User.builder()
				.id(user.getId())
				.email(newEmail)
				.build();

		boolean isUpdated = userMapper.updateEmail(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.admin.updateEmail");
		}

		// update or delete sns_info
		// ...
	}

	@Override
	public void updateAddr(AdminUserAddrDto adminUserAddrDto, Long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.admin.userNotFound");
		}

		User modifyUser = User.builder()
				.id(user.getId())
				.zipcode(adminUserAddrDto.getZipcode())
				.addr(adminUserAddrDto.getAddr())
				.addrDetail(adminUserAddrDto.getAddrDetail())
				.build();

		boolean isUpdated = userMapper.updateAddr(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.admin.updateAddr");
		}
	}

	@Override
	public void updateAgree(AgreeDto agreeDto, Long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.admin.userNotFound");
		}

		User modifyUser = User.builder()
				.id(user.getId())
				.agree(agreeDto.isAgree())
				.build();

		boolean isUpdated = userMapper.updateAgree(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.admin.updateAgree");
		}
	}

	@Override
	public void updateAuth(AdminUserAuthDto adminUserAuthDto, Long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.admin.userNotFound");
		}
		
		Set<String> newRoles = new HashSet<>(adminUserAuthDto.getRoles());
		log.info("\t > newRoles = {}", newRoles); // ["ROLE_USER", "ROLE_MANAGER"]
		
		Set<String> currentRoles = authMapper.findRolesByUserId(user.getId());
		log.info("\t > currentRoles = {}", currentRoles); // ["ROLE_USER", "ROLE_ADMIN"]
		
		// rolesToInsert = newRoles - currentRoles
		Set<String> rolesToInsert = new HashSet<>(newRoles);
		rolesToInsert.removeAll(currentRoles);
		log.info("\t > rolesToInsert = {}", rolesToInsert); // ["ROLE_MANAGER"]

		// rolesToDelete = currentRoles - newRoles
		Set<String> rolesToDelete = new HashSet<>(currentRoles);
		rolesToDelete.removeAll(newRoles);
		log.info("\t > rolesToDelete = {}", rolesToDelete); // ["ROLE_ADMIN"]
		
		for (String role : rolesToDelete) {
			boolean isDeleted = authMapper.deleteAuthByUserIdAndRole(user.getId(), role);
			if (!isDeleted) {
				throw new ExpectedException("error.admin.deleteAuth");
			}
		}
		
		for (String role : rolesToInsert) {
			Auth auth = Auth.builder()
					.userId(user.getId())
					.role(role)
					.build();
			
			boolean isSaved = authMapper.insertAuth(auth);
			log.info("\t > saved auth = {}", auth.getId());
			
			if (!isSaved) {
				throw new ExpectedException("error.admin.saveAuth");
			}
		}
	}

	@Override
	public void updatePassword(AdminUserPasswordChangeDto adminUserPasswordChangeDto, Long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.admin.userNotFound");
		}
		
		String newPassword = adminUserPasswordChangeDto.getNewPassword();
		User modifyUser = User.builder()
				.id(user.getId())
				.password(passwordEncoder.encode(newPassword))
				.build();
		
		boolean isUpdated = userMapper.updatePassword(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.admin.updatePassword");
		}
	}

	@Override
	public void registerUser(AdminUserRegistrationDto adminUserRegistrationDto) {
		String rawPassword = adminUserRegistrationDto.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		
		User user = User.builder()
			.email(adminUserRegistrationDto.getEmail())
			.nickname(adminUserRegistrationDto.getNickname())
			.password(encPassword)
			.agree(false)
			.build();
		
		boolean isUserSaved = userMapper.insertUser(user);
		log.info("\t > saved user, id = {}", user.getId());
		
		if (!isUserSaved) {
			throw new ExpectedException("error.admin.saveUser");
		}
		
		List<String> roles =  new ArrayList<>(Collections.singletonList("ROLE_USER"));;
		roles.addAll(adminUserRegistrationDto.getRoles());
		log.info("\t > roles = {}", roles);
		
		for (String role : roles) {
			Auth auth = Auth.builder()
				.userId(user.getId())
				.role(role)
				.build();
			
			boolean isAuthSaved = authMapper.insertAuth(auth);
			log.info("\t > saved auth, id = {}", auth.getId());
			
			if (!isAuthSaved) {
				throw new ExpectedException("error.admin.saveAuth");
			}
		}
	}

	@Override
	public AdminUserDto getAdminUser(Long userId) {
		AdminUser adminUser = adminMapper.findAdminUserById(userId);
		if (adminUser == null) {
			throw new ExpectedException("error.admin.userNotFound");
		}

		return AdminUserDto.from(adminUser);
	}
	
	@Override
	public int deleteUsers(List<Long> userIds) {
		int deletedRows = adminMapper.deleteUsers(userIds);
		if (deletedRows == 0) {
			throw new ExpectedException("error.admin.deleteUsers");
		}
		
		return deletedRows;
	}

	@Override
	public List<AdminBoardDto> getPagedBoards(AdminBoardCriteria adminBoardCri) {
		log.info("\t > find pagedBoards");
		return adminMapper.findPagedBoards(adminBoardCri)
				.stream()
				.map(adminBoard -> AdminBoardDto.from(adminBoard))
				.collect(Collectors.toList());
	}

	@Override
	public Pagination getBoardPagination(AdminBoardCriteria adminBoardCri) {
		int totalCnt = adminMapper.findTotalCntForBoardPaging(adminBoardCri);
		return (totalCnt > 0) ? new Pagination(totalCnt, adminBoardCri.getPage(), adminBoardCri.getRecordCnt(), pageRange) : null;
	}

	@Override
	public int deleteBoards(List<Long> boardIds) {
		int deletedRows = adminMapper.deleteBoards(boardIds);
		if (deletedRows == 0) {
			throw new ExpectedException("error.admin.deleteBoards");
		}
		
		return deletedRows;
	}

}
