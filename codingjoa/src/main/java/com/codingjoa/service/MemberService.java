package com.codingjoa.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordDto;

public interface MemberService {

	void save(JoinDto joinDto);
	
	boolean isNicknameExist(String memberNickname);
	
	void checkEmailForJoin(String memeberEmail);
	
	void checkEmailForUpdate(String memberEmail, Integer memberIdx);
	
	void checkEmailForReset(String memberEmail);
	
	//String getMemberIdByEmail(String memberEmail);
	
	Integer getMemberIdxByIdAndEmail(String memberId, String memberEmail);
	
	void updateNickname(NicknameDto nicknameDto, Integer memberIdx);
	
	void updateEmail(EmailAuthDto emailAuthDto, Integer memberIdx);
	
	void updateAddr(AddrDto addrDto, Integer memberIdx);
	
	void updateAgree(AgreeDto agreeDto, Integer memberIdx);
	
	void updatePassword(PasswordChangeDto passwordChangeDto, Integer memberIdx);
	
	void updatePassword(PasswordDto passwordDto, Integer memberIdx);
	
	MemberInfoDto getMemberInfoByIdx(Integer memberIdx);

	UserDetails getUserDetailsByIdx(Integer memberIdx);
	
}
