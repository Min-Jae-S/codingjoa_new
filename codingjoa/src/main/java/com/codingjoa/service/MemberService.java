package com.codingjoa.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordSaveDto;

public interface MemberService {

	void saveMember(JoinDto joinDto);
	
	void saveOAuth2Member(String memberNickname, String memberEmail, String provider);
	
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
	
	void savePassword(PasswordSaveDto passwordSaveDto, Integer memberIdx);
	
	MemberInfoDto getMemberInfoByIdx(Integer memberIdx);
	
	Map<String, Object> getUserDetailsByEmail(String memberEmail); // UserDetailsServiceImpl, OAuth2UserServiceImpl

	UserDetails getUserDetailsByIdx(Integer memberIdx);
	
}
