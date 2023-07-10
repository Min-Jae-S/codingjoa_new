package com.codingjoa.service;

import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;

public interface MemberService {

	void register(JoinDto joinDto); // member, auth
	
	boolean isIdExist(String memberId);
	
	boolean isEmailExist(String memberEmail);
	
	boolean isMyEmail(String memberEmail, String memberId);

	void updateEmail(String memberEmail, Integer memberIdx);
	
	void updateAddr(String memberZipcode, String memberAddr, String memberAddrDetail, Integer memberIdx);
	
	void updateAgree(boolean memberAgree, Integer memberIdx);
	
	boolean isMyPassword(String memberPassword, Integer memberIdx);
	
	void updatePassword(String memberPassword, Integer memberIdx);
	
	String findAccount(EmailAuthDto emailAuthDto);
	
	boolean isAccountExist(String memberId, String memberEmail);
	
}
