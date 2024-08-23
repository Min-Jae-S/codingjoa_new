package com.codingjoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Auth;
import com.codingjoa.entity.Member;


@Mapper
public interface MemberMapper {
	
	void insertMember(Member member);
	
	void insertAuth(Auth auth);
	
	boolean isNicknameExist(String memberNickname);
	
	Map<String, Object> findMemberInfoByIdx(Integer memberIdx);
	
	Member findMemberByEmail(String memberEmail);
	
	Member findMemberByIdx(Integer memberIdx);
	
	int updateNickname(Member member);
	
	int updateEmail(Member member);
	
	int updateAddr(Member member);
	
	int updateAgree(Member member);
	
	int updatePassword(Member member);
	
	Member findMemeberByIdAndEmail(@Param("memberId") String memberId, @Param("memberEmail") String memberEmail);
	
	Map<String, Object> findUserDetailsByEmail(String memberEmail); // for authentication in UserDetailsService, OAuth2UserService
	
	Map<String, Object> findUserDetailsByIdx(Integer memberIdx);	// for jwt reissuance after updating
}
