package com.codingjoa.service;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.PasswordDto;

public interface MemberService {

	public void register(JoinDto joinDto); // member, auth
	
	public boolean isIdExist(String memberId);
	
	public boolean isEmailExist(String memberEmail);
	
	public void updateEmail(EmailAuthDto emailAuthDto, String memberId);
	
	public void updateAddr(AddrDto addrDto, String memberId);
	
	public boolean isMyEmail(String memberEmail, String memberId);
	
	public void updateAgree(AgreeDto agreeDto, String memberId);
	
	public boolean isMyPassword(String memberPassword, String memberId);
	
	public void updatePassword(PasswordDto passwordDto, String memberId);
	
	public String findAccount(EmailAuthDto emailAuthDto);
	
}
