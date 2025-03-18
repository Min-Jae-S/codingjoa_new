package com.codingjoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.Auth;
import com.codingjoa.entity.SnsInfo;
import com.codingjoa.entity.User;


@Mapper
public interface UserMapper {
	
	boolean insertUser(User user);
	
	boolean insertAuth(Auth auth);
	
	boolean insertSnsInfo(SnsInfo snsInfo);
	
	boolean isNicknameExist(String nickname);
	
	Map<String, Object> findAccountById(Long userId);
	
	User findUserByEmail(String email);
	
	User findUserById(Long userId);
	
	boolean updateNickname(User user);
	
	boolean updateEmail(User user);
	
	boolean updateAddr(User user);
	
	boolean updateAgree(User user);
	
	boolean updatePassword(User user);
	
	// for authentication (UserDetailsService, OAuth2UserService)
	Map<String, Object> findUserDetailsByEmail(String email);
	
	// for JWT re-issuance after updating account
	Map<String, Object> findUserDetailsById(Long userId);
}
