package com.codingjoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Auth;
import com.codingjoa.entity.Member;

@Mapper
public interface MemberMapper {
	
	public int registerMember(Member member);
	
	public void registerAuth(Auth auth);
	
	public boolean isIdExist(String memberId);
	
	public boolean isEmailExist(String memberEmail);
	
	public Map<String, Object> findUserDetailsById(String memberId); // Member, memberRole
	
	public void updateEmail(@Param("memberEmail") String memberEmail, @Param("memberId") String memberId);
	
	public String findEmailById(String memberId);
	
	public void updateAddr(@Param("memberZipcode") String memberZipcode, 
							@Param("memberAddr") String memberAddr,
							@Param("memberAddrDetail") String memberAddrDetail,
							@Param("memberId") String memberId);
	
	public void updateAgree(@Param("memberAgree") boolean memberAgree, @Param("memberId") String memberId);
	
	public String findPasswordById(String memberId);
	
	public void updatePassword(@Param("memberPassword") String memberPassword, @Param("memberId") String memberId);
	
	public String findIdbyEmail(String memberEmail);
	
	
}
