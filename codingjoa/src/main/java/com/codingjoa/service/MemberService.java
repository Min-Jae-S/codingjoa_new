package com.codingjoa.service;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.PasswordDto;

public interface MemberService {

	void register(JoinDto joinDto); // member, auth
	
	boolean isIdExist(String memberId);
	
	boolean isEmailExist(String memberEmail);
	
	void updateEmail(String memberEmail, Integer memberIdx);
	
	void updateAddr(AddrDto addrDto, String memberId);
	
	boolean isMyEmail(String memberEmail, String memberId);
	
	void updateAgree(AgreeDto agreeDto, String memberId);
	
	boolean isMyPassword(String memberPassword, String memberId);
	
	void updatePassword(PasswordDto passwordDto, String memberId);
	
	String findAccount(EmailAuthDto emailAuthDto);
	
	boolean isAccountExist(String memberId, String memberEmail);
	
}
