package com.codingjoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Auth;
import com.codingjoa.entity.Member;


@Mapper
public interface MemberMapper {
	
	int registerMember(Member member);
	
	void registerAuth(Auth auth);
	
	boolean isIdExist(String memberId);
	
	Member findMemberByEmail(String memberEmail);
	
	boolean isEmailExist(String memberEmail);
	
	String findIdByEmail(String memberEmail);
	
	String findEmailById(String memberId);

	// Member, memberRole, boardLikesList, commentLikesList
	Map<String, Object> findUserDetailsById(String memberId); 
	
	Member findMemberByIdx(Integer memberIdx);
	
	void updateEmail(Member member);
	
	void updateAddr(Member member);
	
	void updateAgree(Member member);
	
	void updatePassword(Member member);
	
	Member findMemeberByIdAndEmail(@Param("memberId") String memberId, @Param("memberEmail") String memberEmail);
	
}
