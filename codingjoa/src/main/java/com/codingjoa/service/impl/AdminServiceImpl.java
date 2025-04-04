package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminUserAuthDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.dto.AdminUserPasswordChangeDto;
import com.codingjoa.dto.AdminUserRegistrationDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.entity.AdminUser;
import com.codingjoa.entity.User;
import com.codingjoa.error.ExpectedException;
import com.codingjoa.mapper.AdminMapper;
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
	private final int pageRange;
	
	public AdminServiceImpl(AdminMapper adminMapper, UserMapper userMapper, @Value("${pagination.pageRange}") int pageRange) {
		this.adminMapper = adminMapper;
		this.userMapper = userMapper;
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
		if (!currentEmail.equals(newEmail) && userMapper.findUserByEmail(newEmail) != null) {
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
	}

	@Override
	public void updateAddr(AddrDto addrDto, Long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.admin.userNotFound");
		}
		
		User modifyUser = User.builder()
				.id(user.getId())
				.zipcode(addrDto.getZipcode())
				.addr(addrDto.getAddr())
				.addrDetail(addrDto.getAddrDetail())
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePassword(AdminUserPasswordChangeDto adminUserPasswordChangeDto, Long userId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void saveUser(AdminUserRegistrationDto adminUserRegistrationDto, Long userId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public AdminUserDto getAdminUser(Long userId) {
		AdminUser adminUser = adminMapper.findAdminUser(userId);
		if (adminUser == null) {
			throw new ExpectedException("error.admin.userNotFound");
		}
		
		return AdminUserDto.from(adminUser);
	}
	
	@Override
	public Pagination getUserPagination(AdminUserCriteria adminUserCri) {
		int totalCnt = adminMapper.findTotalCntForUserPaging(adminUserCri);
		return (totalCnt > 0) ? new Pagination(totalCnt, adminUserCri.getPage(), adminUserCri.getRecordCnt(), pageRange) : null;
	}
	
	@Override
	public int deleteUsers(List<Long> userIds) {
		return adminMapper.deleteUsers(userIds);
	}
	
	@Override
	public List<AdminBoardDto> getPagedBoards(AdminBoardCriteria adminBoardCri) {
		log.info("\t > find pagedBoards");
		return adminMapper.findPagedBoards(adminBoardCri)
				.stream()
				.map(adminBoard ->  AdminBoardDto.from(adminBoard))
				.collect(Collectors.toList());
	}
	
	@Override
	public Pagination getBoardPagination(AdminBoardCriteria adminBoardCri) {
		int totalCnt = adminMapper.findTotalCntForBoardPaging(adminBoardCri);
		return (totalCnt > 0) ? new Pagination(totalCnt, adminBoardCri.getPage(), adminBoardCri.getRecordCnt(), pageRange) : null;
	}

	@Override
	public int deleteBoards(List<Long> boardIds) {
		return adminMapper.deleteBoards(boardIds);
	}


}
