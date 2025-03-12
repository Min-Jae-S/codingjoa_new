package com.codingjoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Auth;
import com.codingjoa.entity.User;
import com.codingjoa.entity.SnsInfo;


@Mapper
public interface UserMapper {
	
	boolean insertUser(User user);
	
	boolean insertAuth(Auth auth);
	
	boolean insertSnsInfo(SnsInfo snsInfo);
	
	boolean isNicknameExist(String nickname);
	
	Map<String, Object> findUserInfoById(Long userId);
	
	User findUserByEmail(String email);
	
	User findUserById(Long userId);
	
	boolean updateNickname(User user);
	
	boolean updateEmail(User user);
	
	boolean updateAddr(User user);
	
	boolean updateAgree(User user);
	
	boolean updatePassword(User user);
	
	User findUserByIdAndEmail(@Param("id") String id, @Param("email") String email);
	
	Map<String, Object> findUserDetailsByEmail(String email); 	// for authentication in UserDetailsService, OAuth2UserService
	
	Map<String, Object> findUserDetailsById(Long userId);		// for JWT re-issuance after updating
}
