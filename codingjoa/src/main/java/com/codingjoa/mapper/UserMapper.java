package com.codingjoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Auth;
import com.codingjoa.entity.User;
import com.codingjoa.entity.SnsInfo;


@Mapper
public interface UserMapper {
	
	boolean insertMember(User member);
	
	boolean insertAuth(Auth auth);
	
	boolean insertSnsInfo(SnsInfo snsInfo);
	
	boolean isNicknameExist(String memberNickname);
	
	Map<String, Object> findMemberInfoByIdx(Integer memberIdx);
	
	User findMemberByEmail(String memberEmail);
	
	User findMemberByIdx(Integer memberIdx);
	
	boolean updateNickname(User member);
	
	boolean updateEmail(User member);
	
	boolean updateAddr(User member);
	
	boolean updateAgree(User member);
	
	boolean updatePassword(User member);
	
	User findMemeberByIdAndEmail(@Param("memberId") String memberId, @Param("memberEmail") String memberEmail);
	
	Map<String, Object> findUserDetailsByEmail(String memberEmail); // for authentication in UserDetailsService, OAuth2UserService
	
	Map<String, Object> findUserDetailsByIdx(Integer memberIdx);	// for JWT re-issuance after updating
}
