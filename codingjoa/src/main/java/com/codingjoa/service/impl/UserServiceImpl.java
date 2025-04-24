package com.codingjoa.service.impl;

import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.AccountDto;
import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordSaveDto;
import com.codingjoa.entity.Auth;
import com.codingjoa.entity.SnsInfo;
import com.codingjoa.entity.User;
import com.codingjoa.enums.OAuth2LoginStatus;
import com.codingjoa.error.ExpectedException;
import com.codingjoa.mapper.AuthMapper;
import com.codingjoa.mapper.SnsMapper;
import com.codingjoa.mapper.UserMapper;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.oauth2.OAuth2Attributes;
import com.codingjoa.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final UserMapper userMapper;
	private final AuthMapper authMapper;
	private final SnsMapper snsMapper;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void saveUser(JoinDto joinDto) {
		String rawPassword = joinDto.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		joinDto.setPassword(encPassword);
		
		User user = joinDto.toEntity();
		boolean isUserSaved = userMapper.insertUser(user);
		log.info("\t > saved user, id = {}", user.getId());
		
		if (!isUserSaved) {
			throw new ExpectedException("error.user.saveUser");
		}
		
		Auth auth = Auth.builder()
				.userId(user.getId())
				.role("ROLE_USER")
				.build();
		
		boolean isAuthSaved = authMapper.insertAuth(auth);
		log.info("\t > saved auth, id = {}", auth.getId());
		
		if (!isAuthSaved) {
			throw new ExpectedException("error.user.saveAuth");
		}
	}
	
	@Override
	public boolean isNicknameExist(String nickname) {
		return userMapper.isNicknameExist(nickname);
	}
	
	@Override
	public boolean isEmailExist(String email) {
		return userMapper.isEmailExist(email);
	}

	@Override
	public void checkEmailForJoin(String email) {
		User user = userMapper.findUserByEmail(email);
		if (user != null) {
			throw new ExpectedException("error.auth.join.emailExists", "email");
		}
	}
	
	@Override
	public AccountDto getAccountById(Long userId) {
		Map<String, Object> accountMap = userMapper.findAccountById(userId);
		if (accountMap == null) {
			throw new ExpectedException("error.user.notFound");
		}
		
		return AccountDto.from(accountMap);
	}
	
	@Override
	public void checkEmailForUpdate(String email, Long userId) {
		User userById = getUser(userId);
		String currentEmail = userById.getEmail();
		if (email.equals(currentEmail)) {
			throw new ExpectedException("error.user.notCurrentEmail", "email");
		}
		
		User userByEmail = userMapper.findUserByEmail(email);
		if (userByEmail != null) {
			throw new ExpectedException("error.user.emailExists", "email");
		}
	}
	
	@Override
	public Long checkEmailForReset(String email) {
		User user = userMapper.findUserByEmail(email);
		if (user == null) {
			throw new ExpectedException("error.reset-password.emailNotExists", "email");
		}
		
		return user.getId();
	}
	
	@Override
	public void updateNickname(NicknameDto nicknameDto, Long userId) {
		User user = getUser(userId);
		String currentNickname = user.getNickname();
		String newNickname = nicknameDto.getNickname();
		if (!newNickname.equals(currentNickname) && userMapper.isNicknameExist(newNickname)) {
			throw new ExpectedException("error.user.nicknameExists", "nickname");
		}
		
		User modifyUser = User.builder()
				.id(user.getId())
				.nickname(newNickname)
				.build();
		
		boolean isUpdated = userMapper.updateNickname(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.user.updateNickname");
		}
	}

	@Override
	public void updateEmail(EmailAuthDto emailAuthDto, Long userId) {
		User user = getUser(userId);
		User modifyUser = User.builder()
				.id(user.getId())
				.email(emailAuthDto.getEmail())
				.build();
		
		boolean isUpdated = userMapper.updateEmail(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.user.updateEmail");
		}
		
		// update or delete sns_info
		// ...
	}
	
	@Override
	public void updateAddr(AddrDto addrDto, Long userId) {
		User user = getUser(userId);
		User modifyUser = User.builder()
				.id(user.getId())
				.zipcode(addrDto.getZipcode())
				.addr(addrDto.getAddr())
				.addrDetail(addrDto.getAddrDetail())
				.build();
		
		boolean isUpdated = userMapper.updateAddr(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.user.updateAddr");
		}
	}

	@Override
	public void updateAgree(AgreeDto agreeDto, Long userId) {
		User user = getUser(userId);
		User modifyUser = User.builder()
				.id(user.getId())
				.agree(agreeDto.isAgree())
				.build();
		
		boolean isUpdated = userMapper.updateAgree(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.user.updateAgree");
		}
	}
	
	@Override
	public void updatePassword(PasswordChangeDto passwordChangeDto, Long userId) {
		User user = getUser(userId);
		String password = user.getPassword();
		String currentPassword = passwordChangeDto.getCurrentPassword();
		if (!passwordEncoder.matches(currentPassword, password)) {
			throw new ExpectedException("error.user.mismatchPassword", "currentPassword");
		}
		
		String newPassword = passwordChangeDto.getNewPassword();
		if (passwordEncoder.matches(newPassword, password)) {
			throw new ExpectedException("error.user.samePassword", "newPassword");
		}
		
		User modifyUser = User.builder()
				.id(user.getId())
				.password(passwordEncoder.encode(newPassword))
				.build();
		
		boolean isUpdated = userMapper.updatePassword(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.user.updatePassword");
		}
	}
	
	@Override
	public void savePassword(PasswordSaveDto passwordSaveDto, Long userId) {
		User user = getUser(userId);
		if (user.getPassword() != null) {
			throw new ExpectedException("error.user.hasPassword");
		}
		
		String rawPassword = passwordSaveDto.getNewPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		
		User modifyUser = User.builder()
				.id(user.getId())
				.password(encPassword)
				.build();
		
		boolean isSaved = userMapper.updatePassword(modifyUser);
		if (!isSaved) {
			throw new ExpectedException("error.user.savePassword");
		}
	}
	
	@Override
	public void resetPassword(String newPassword, Long userId) {
		User user = getUser(userId);
		String encPassword = passwordEncoder.encode(newPassword);
		
		User modifyUser = User.builder()
				.id(user.getId())
				.password(encPassword)
				.build();
		
		boolean isUpdated = userMapper.updatePassword(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.reset-password.updatePassword");
		}
	}
	
	@Override
	public User getUser(Long userId) {
		User user = userMapper.findUserById(userId);
		log.info("\t > found user = {}", user);
		
		if (user == null) {
			throw new ExpectedException("error.user.notFound");
		}
		
		return user;
	}
	
	@Override
	public PrincipalDetails getUserDetailsByEmail(String email) {
		Map<String, Object> userDetailsMap = userMapper.findUserDetailsByEmail(email);
		return (userDetailsMap == null) ? null : PrincipalDetails.from(userDetailsMap);
	}
	
	@Override
	public PrincipalDetails getUserDetailsById(Long userId) {
		Map<String, Object> userDetailsMap = userMapper.findUserDetailsById(userId);
		if (userDetailsMap == null) {
			throw new ExpectedException("error.user.notFound");
		}
		
		return PrincipalDetails.from(userDetailsMap);
	}
	
	@Override
	public PrincipalDetails processOAuth2Login(OAuth2Attributes oAuth2Attributes) {
		log.info("## processOAuth2Login");
		
		String email = oAuth2Attributes.getEmail();
		Map<String, Object> userDetailsMap = userMapper.findUserDetailsByEmail(email);
		OAuth2LoginStatus loginStatus;
		
		if (userDetailsMap == null) {
			log.info("\t > no existing user found. Registering new user with OAuth2 account");
			userDetailsMap = saveOAuth2User(oAuth2Attributes);
			loginStatus = OAuth2LoginStatus.NEW;
		} else {
			Long userId = (Long) userDetailsMap.get("id");
			SnsInfo snsInfo = snsMapper.findSnsInfoByUserId(userId);
			
			if (snsInfo == null) {
				log.info("\t > existing user found with local account. Linking OAuth2 account to existing user");
				userDetailsMap = linkOAuth2User(oAuth2Attributes, userId);
				loginStatus = OAuth2LoginStatus.LINKED;
			} else {
				log.info("\t > OAuth2 account is already linked to the existing user. Proceeding with login");
				loginStatus = OAuth2LoginStatus.LOGGED_IN;
			}
		}
		
		return PrincipalDetails.from(userDetailsMap, oAuth2Attributes, loginStatus);
	}
	
	private Map<String, Object> saveOAuth2User(OAuth2Attributes oAuth2Attributes) {
		String nickname = resolveNickname(oAuth2Attributes.getNickname());
		User user = User.builder()
				.nickname(nickname) 
				.email(oAuth2Attributes.getEmail())
				.agree(false)
				.build();
		
		boolean isUserSaved = userMapper.insertUser(user);
		log.info("\t > saved user, id = {}", user.getId());
		
		if (!isUserSaved) {
			throw new ExpectedException("error.user.saveUser");
		}
		
		SnsInfo snsInfo = SnsInfo.builder()
				.userId(user.getId())
				.snsId(oAuth2Attributes.getId())
				.provider(oAuth2Attributes.getProvider())
				.build();
		
		boolean isSnsInfoSaved = snsMapper.insertSnsInfo(snsInfo);
		log.info("\t > saved snsInfo, id = {}", snsInfo.getId());
		
		if (!isSnsInfoSaved) {
			throw new ExpectedException("error.user.saveSnsInfo");
		}

		Auth auth = Auth.builder()
				.userId(user.getId())
				.role("ROLE_USER")
				.build();
		
		boolean isAuthSaved = authMapper.insertAuth(auth);
		log.info("\t > saved auth, id = {}", auth.getId());
		
		if (!isAuthSaved) {
			throw new ExpectedException("error.user.saveAuth");
		}
		
		return userMapper.findUserDetailsById(user.getId());
	}
	
	private String resolveNickname(String nickname) {
		final int MAX_LENGTH = 10;
		final int RANDOM_SUFFIX_LENGTH = 4;
		final int MAX_BASE_LENGTH = MAX_LENGTH - RANDOM_SUFFIX_LENGTH;
		
		nickname = nickname.replaceAll("\\s+", ""); // ex) google: 'MinJae Suh' --> 'MinJaeSuh'
		if (nickname.length() > MAX_LENGTH) {
			nickname = nickname.substring(0, MAX_LENGTH);
		}
		
		if (userMapper.isNicknameExist(nickname)) {
	        String baseNickname = (nickname.length() > MAX_BASE_LENGTH) ? nickname.substring(0, MAX_BASE_LENGTH) : nickname;
			do {
				log.info("\t > nickname '{}' exists, generating new nickname based on '{}'", nickname, baseNickname);
				nickname = baseNickname + RandomStringUtils.randomNumeric(RANDOM_SUFFIX_LENGTH);
			} while (userMapper.isNicknameExist(nickname)); 
		}
		
		log.info("\t > resolved nickname = {}", nickname);
		
		return nickname;
	}
	
	private Map<String, Object> linkOAuth2User(OAuth2Attributes oAuth2Attributes, Long userId) {
		SnsInfo snsInfo = SnsInfo.builder()
				.userId(userId)
				.snsId(oAuth2Attributes.getId())
				.provider(oAuth2Attributes.getProvider())
				.build();
		
		boolean isSnsInfoSaved = snsMapper.insertSnsInfo(snsInfo);
		log.info("\t > saved snsInfo, id = {}", snsInfo.getId());
		
		if (!isSnsInfoSaved) {
			throw new ExpectedException("error.user.saveSnsInfo");
		}
		
		return userMapper.findUserDetailsById(userId);
	}
	

}
