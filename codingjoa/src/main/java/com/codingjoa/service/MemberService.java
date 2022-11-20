package com.codingjoa.service;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.UpdatePasswordDto;

public interface MemberService {

	public void register(JoinDto joinDto); // member, auth
	
	public boolean isIdExist(String memberId);
	
	public boolean isEmailExist(String memberEmail);
	
	public void updateEmail(EmailDto emailDto, String memberId);
	
	public void updateAddr(AddrDto addrDto, String memberId);
	
	public boolean isMyEmail(String memberEmail, String memberId);
	
	public void updateAgree(AgreeDto agreeDto, String memberId);
	
	public boolean isMyPassword(String memberPassword, String memberId);
	
	public void updatePassword(UpdatePasswordDto updatePasswordDto, String memberId);
	
}
