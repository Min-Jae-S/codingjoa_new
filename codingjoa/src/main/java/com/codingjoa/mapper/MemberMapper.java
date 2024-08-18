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
	
	void updateEmail(Member member);
	
	void updateAddr(Member member);
	
	void updateAgree(Member member);
	
	void updatePassword(Member member);
	
	Member findMemeberByIdAndEmail(@Param("memberId") String memberId, @Param("memberEmail") String memberEmail);
	
	Map<String, Object> findUserDetailsByEmail(String memberEmail); // using in UserDetailsService, OAuth2UserService
	
	Map<String, Object> findUserDetailsByIdx(Integer memberIdx);
}
