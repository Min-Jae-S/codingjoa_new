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
	
	boolean isEmailExist(String memberEmail);
	
	Map<String, Object> findUserDetailsById(String memberId); // Member, memberRole
	
	void updateEmail(@Param("memberEmail") String memberEmail, @Param("memberId") String memberId);
	
	String findEmailById(String memberId);
	
	void updateAddr(@Param("memberZipcode") String memberZipcode, 
					@Param("memberAddr") String memberAddr,
					@Param("memberAddrDetail") String memberAddrDetail,
					@Param("memberId") String memberId);
	
	void updateAgree(@Param("memberAgree") boolean memberAgree, @Param("memberId") String memberId);
	
	String findPasswordById(String memberId);
	
	void updatePassword(@Param("memberPassword") String memberPassword, @Param("memberId") String memberId);
	
	String findIdbyEmail(String memberEmail);
	
	boolean isAccountExist(@Param("memberId") String memberId, @Param("memberEmail") String memberEmail);
	
	
}
