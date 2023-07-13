package com.codingjoa.service;

import com.codingjoa.dto.JoinDto;

public interface MemberService {

	void register(JoinDto joinDto); // member, auth
	
	boolean isIdExist(String memberId);
	
	boolean isEmailExist(String memberEmail);
	
	String findIdByEmail(String memberEmail);
	
	boolean isMyEmail(String memberEmail, String memberId);

	void updateEmail(String memberEmail, Integer memberIdx);
	
	void updateAddr(String memberZipcode, String memberAddr, String memberAddrDetail, Integer memberIdx);
	
	void updateAgree(boolean memberAgree, Integer memberIdx);
	
	void checkCurrentPassword(String memberPassword, Integer memberIdx);
	
	void updatePassword(String memberPassword, Integer memberIdx);
	
	boolean isAccountExist(String memberId, String memberEmail);
	
}
