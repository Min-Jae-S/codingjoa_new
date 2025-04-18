package com.codingjoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.User;


@Mapper
public interface UserMapper {
	
	boolean insertUser(User user);
	
	boolean isNicknameExist(String nickname);

	boolean isEmailExist(String email);
	
	Map<String, Object> findAccountById(Long userId);
	
	User findUserByEmail(String email);
	
	User findUserById(Long userId);
	
	boolean updateNickname(User user);
	
	boolean updateEmail(User user);
	
	boolean updateAddr(User user);
	
	boolean updateAgree(User user);
	
	boolean updatePassword(User user);
	
	Map<String, Object> findUserDetailsByEmail(String email); // for authentication (UserDetailsService, OAuth2UserService)
	
	Map<String, Object> findUserDetailsById(Long userId); // for JWT re-issuance after updating account
}
