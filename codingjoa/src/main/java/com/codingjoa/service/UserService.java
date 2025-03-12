package com.codingjoa.service;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordSaveDto;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.oauth2.OAuth2Attributes;

public interface UserService {

	void saveMember(JoinDto joinDto);
	
	void saveOAuth2Member(OAuth2Attributes oAuth2Attributes);
	
	void connectOAuth2Member(OAuth2Attributes oAuth2Attributes, Integer memberIdx);
	
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
	
	AdminUserDto getMemberInfoByIdx(Integer memberIdx);
	
	PrincipalDetails getUserDetailsByEmail(String memberEmail); // for authentication in UserDetailsService, OAuth2UserService

	PrincipalDetails getUserDetailsByIdx(Integer memberIdx);	// for JWT re-issuance after updating
	
}
