package com.codingjoa.service;

import com.codingjoa.dto.JoinDto;

public interface MemberService {

	void save(JoinDto joinDto); // member, auth
	
	boolean isIdExist(String memberId);
	
	void checkEmailForJoin(String memeberEmail);
	
	void checkEmailForUpdate(String memberEmail, Integer memberIdx);
	
	void checkEmailForReset(String memberEmail);
	
	String getMemberIdByEmail(String memberEmail);
	
	Integer getMemberIdxByIdAndEmail(String memberId, String memberEmail);
	
	void updateEmail(String memberEmail, Integer memberIdx);
	
	void updateAddr(String memberZipcode, String memberAddr, String memberAddrDetail, Integer memberIdx);
	
	void updateAgree(boolean memberAgree, Integer memberIdx);
	
	void checkCurrentPassword(String memberPassword, Integer memberIdx);
	
	void updatePassword(String memberPassword, Integer memberIdx);
	
}
