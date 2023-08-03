package com.codingjoa.service;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordDto;

public interface MemberService {

	void save(JoinDto joinDto); // member, auth
	
	boolean isIdExist(String memberId);
	
	void checkEmailForJoin(String memeberEmail);
	
	void checkEmailForUpdate(String memberEmail, Integer memberIdx);
	
	void checkEmailForReset(String memberEmail);
	
	String getMemberIdByEmail(String memberEmail);
	
	Integer getMemberIdxByIdAndEmail(String memberId, String memberEmail);
	
	void updateEmail(EmailAuthDto emailAuthDto, Integer memberIdx);
	
	void updateAddr(AddrDto addrDto, Integer memberIdx);
	
	void updateAgree(AgreeDto agreeDto, Integer memberIdx);
	
	void checkCurrentPassword(PasswordDto passwordDto, Integer memberIdx);
	
	void updatePassword(PasswordChangeDto passwordChangeDto, Integer memberIdx);
	
}
