package com.codingjoa.service.impl;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordSaveDto;
import com.codingjoa.dto.UserInfoDto;
import com.codingjoa.entity.Auth;
import com.codingjoa.entity.SnsInfo;
import com.codingjoa.entity.User;
import com.codingjoa.exception.ExpectedException;
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
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void saveUser(JoinDto joinDto) {
		String rawPassword = joinDto.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		joinDto.setPassword(encPassword);
		
		User user = joinDto.toEntity();
		log.info("\t > convert JoinDto to user entity = {}", user);
		
		boolean isUserSaved = userMapper.insertUser(user);
		log.info("\t > saved user = {}", user);
		
		if (!isUserSaved) {
			throw new ExpectedException("error.SaveUser");
		}
		
		Auth auth = Auth.builder()
				.userId(user.getId())
				.role("ROLE_MEMBER")
				.build();
		log.info("\t > create auth entity = {}", auth);
		
		boolean isAuthSaved = userMapper.insertAuth(auth);
		log.info("\t > saved auth = {}", auth);
		
		if (!isAuthSaved) {
			throw new ExpectedException("error.SaveAuth");
		}
	}
	
	@Override
	public void saveOAuth2User(OAuth2Attributes oAuth2Attributes) {
		String nickname = resolveNickname(oAuth2Attributes.getNickname());
		log.info("\t > resolved nickname = {}", nickname);
		
		User user = User.builder()
				.nickname(nickname) 
				.email(oAuth2Attributes.getEmail())
				.agree(false)
				.build();
		log.info("\t > create user entity = {}", user);
		
		boolean isUserSaved = userMapper.insertUser(user);
		log.info("\t > saved user = {}", user);
		
		if (!isUserSaved) {
			throw new ExpectedException("error.SaveUser");
		}
		
		SnsInfo snsInfo = SnsInfo.builder()
				.userId(user.getId())
				.snsId(oAuth2Attributes.getId())
				.provider(oAuth2Attributes.getProvider())
				.build();
		log.info("\t > create snsInfo entity = {}", snsInfo);
		
		boolean isSnsInfoSaved = userMapper.insertSnsInfo(snsInfo);
		log.info("\t > saved snsInfo = {}", snsInfo);
		
		if (!isSnsInfoSaved) {
			throw new ExpectedException("error.SaveSnsInfo");
		}

		Auth auth = Auth.builder()
				.userId(user.getId())
				.role("ROLE_MEMBER")
				.build();
		log.info("\t > create auth entity = {}", auth);
		
		boolean isAuthSaved = userMapper.insertAuth(auth);
		log.info("\t > saved auth = {}", auth);
		
		if (!isAuthSaved) {
			throw new ExpectedException("error.SaveAuth");
		}
	}
	
	private String resolveNickname(String nickname) {
		final int MAX_NICKNAME_LENGTH = 10;
		final int RANDOM_SUFFIX_LENGTH = 4;
		final int MAX_BASE_NICKNAME_LENGTH = MAX_NICKNAME_LENGTH - RANDOM_SUFFIX_LENGTH;
		
		nickname = nickname.replaceAll("\\s+", ""); // google: MinJae Suh --> MinJaeSuh
		if (nickname.length() > MAX_NICKNAME_LENGTH) {
			nickname = nickname.substring(0, MAX_NICKNAME_LENGTH);
		}
		
		if (userMapper.isNicknameExist(nickname)) {
	        String baseNickname = (nickname.length() > MAX_BASE_NICKNAME_LENGTH) 
	        		? nickname.substring(0, MAX_BASE_NICKNAME_LENGTH) 
	        		: nickname;
			do {
				log.info("\t > create new nickname based on '{}' due to conflict: {}", baseNickname, nickname);
				nickname = baseNickname + RandomStringUtils.randomNumeric(RANDOM_SUFFIX_LENGTH);
			} while (userMapper.isNicknameExist(nickname)); 
		}
		
		return nickname;
	}
	
	@Override
	public void connectOAuth2User(OAuth2Attributes oAuth2Attributes, Long userId) {
		SnsInfo snsInfo = SnsInfo.builder()
				.userId(userId)
				.snsId(oAuth2Attributes.getId())
				.provider(oAuth2Attributes.getProvider())
				.connectedAt(LocalDateTime.now())
				.build();
		log.info("\t > create snsInfo entity = {}", snsInfo);
		
		userMapper.insertSnsInfo(snsInfo);
	}
	
	@Override
	public boolean isNicknameExist(String nickname) {
		return userMapper.isNicknameExist(nickname);
	}

	@Override
	public void checkEmailForJoin(String email) {
		User user = userMapper.findUserByEmail(email);
		if (user != null) {
			throw new ExpectedException("error.EmailExist", "email");
		}
	}
	
	@Override
	public void checkEmailForUpdate(String email, long userId) {
		User userById = userMapper.findUserById(userId);
		if (userById == null) {
			throw new ExpectedException("error.NotFoundUser", "email");
		}
		
		String currentEmail = userById.getEmail();
		if (email.equals(currentEmail)) {
			throw new ExpectedException("error.NotCurrentEmail", "email");
		}
		
		User userByEmail = userMapper.findUserByEmail(email);
		if (userByEmail != null) {
			throw new ExpectedException("error.EmailExist", "email");
		}
	}
	
	@Override
	public void checkEmailForReset(String email) {
		User user = userMapper.findUserByEmail(email);
		if (user == null) {
			throw new ExpectedException("error.NotEmailExist", "email");
		}
	}
	
	@Override
	public void updateNickname(NicknameDto nicknameDto, long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.NotFoundUser");
		}
		
		String currentNickname = user.getNickname();
		String newNickname = nicknameDto.getNickname();
		if (!newNickname.equals(currentNickname) && userMapper.isNicknameExist(newNickname)) {
			throw new ExpectedException("error.NicknameExist", "nickname");
		}
		
		User modifyUser = User.builder()
				.id(user.getId())
				.nickname(newNickname)
				.build();
		
		boolean isUpdated = userMapper.updateNickname(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.UpdateNickname");
		}
	}

	@Override
	public void updateEmail(EmailAuthDto emailAuthDto, long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.NotFoundUser");
		}
		
		User modifyUser = User.builder()
				.id(user.getId())
				.email(emailAuthDto.getEmail())
				.build();
		
		boolean isUpdated = userMapper.updateEmail(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.UpdateEmail");
		}
		
		// update snsInfo
		// ...
	}
	
	@Override
	public void updateAddr(AddrDto addrDto, long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.NotFoundUser");
		}
		
		User modifyUser = User.builder()
				.id(user.getId())
				.zipcode(addrDto.getZipcode())
				.addr(addrDto.getAddr())
				.addrDetail(addrDto.getAddrDetail())
				.build();
		
		boolean isUpdated = userMapper.updateAddr(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.UpdateAddr");
		}
	}

	@Override
	public void updateAgree(AgreeDto agreeDto, long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.NotFoundUser");
		}
		
		User modifyUser = User.builder()
				.id(user.getId())
				.agree(agreeDto.isAgree())
				.build();
		
		boolean isUpdated = userMapper.updateAgree(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.UpdateAgree");
		}
	}
	
	@Override
	public void updatePassword(PasswordChangeDto passwordChangeDto, long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.NotFoundUser");
		}
		
		String password = user.getPassword();
		String currentPassword = passwordChangeDto.getCurrentPassword();
		if (!passwordEncoder.matches(currentPassword, password)) {
			throw new ExpectedException("error.MismatchPassword", "currentPassword");
		}
		
		String newPassword = passwordChangeDto.getNewPassword();
		if (passwordEncoder.matches(newPassword, password)) {
			throw new ExpectedException("error.SameAsPassword", "newPassword");
		}
		
		User modifyUser = User.builder()
				.id(user.getId())
				.password(passwordEncoder.encode(newPassword))
				.build();
		
		boolean isUpdated = userMapper.updatePassword(modifyUser);
		if (!isUpdated) {
			throw new ExpectedException("error.UpdatePassword");
		}
	}
	
	@Override
	public void savePassword(PasswordSaveDto passwordSaveDto, long userId) {
		User user = userMapper.findUserById(userId);
		if (user == null) {
			throw new ExpectedException("error.NotFoundUser");
		}
		
		String newPassword = passwordSaveDto.getNewPassword();
		User modifyUser = User.builder()
				.id(user.getId())
				.password(passwordEncoder.encode(newPassword))
				.build();
		
		boolean isSaved = userMapper.updatePassword(modifyUser);
		if (!isSaved) {
			throw new ExpectedException("error.SavePassword");
		}
	}
	
	@Override
	public UserInfoDto getUserInfoById(Long userId) {
		Map<String, Object> userInfoMap = userMapper.findUserInfoById(userId);
		if (userInfoMap == null) {
			throw new ExpectedException("error.NotFoundUser");
		}
		
		return UserInfoDto.from(userInfoMap);
	}
	
	@Override
	public PrincipalDetails getUserDetailsByEmail(String email) {
		Map<String, Object> userDetailsMap = userMapper.findUserDetailsByEmail(email);
		return (userDetailsMap == null) ? null : PrincipalDetails.from(userDetailsMap);
	}
	
	@Override
	public PrincipalDetails getUserDetailsById(long userId) {
		Map<String, Object> userDetailsMap = userMapper.findUserDetailsById(userId);
		if (userDetailsMap == null) {
			throw new ExpectedException("error.NotFoundUser");
		}
		
		return PrincipalDetails.from(userDetailsMap);
	}

}
