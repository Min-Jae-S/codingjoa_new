package com.codingjoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Auth;
import com.codingjoa.entity.Member;


@Mapper
public interface MemberMapper {
	
	int insertMember(Member member);
	
	void insertAuth(Auth auth);
	
	boolean isIdExist(String memberId);
	
	Member findMemberByEmail(String memberEmail);
	
	// using in UserDetailsServiceImpl
	// key = [Member, memberRole, myBoardLikes, myCommentLikes, myProfileImage]
	Map<String, Object> findUserDetailsById(String memberId); 
	
	Member findMemberByIdx(Integer memberIdx);
	
	void updateEmail(Member member);
	
	void updateAddr(Member member);
	
	void updateAgree(Member member);
	
	void updatePassword(Member member);
	
	Member findMemeberByIdAndEmail(@Param("memberId") String memberId, @Param("memberEmail") String memberEmail);
	
	// using in OAuth2UserServiceImpl
	Map<String, Object> findUserDetailsByEmail(String memberEmail);
	
}
