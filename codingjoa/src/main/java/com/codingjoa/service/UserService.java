package com.codingjoa.service;

import com.codingjoa.dto.AccountDto;
import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordSaveDto;
import com.codingjoa.entity.SnsInfo;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.oauth2.OAuth2Attributes;

public interface UserService {

	void saveUser(JoinDto joinDto);
	
	void saveOAuth2User(OAuth2Attributes oAuth2Attributes);
	
	void connectOAuth2User(OAuth2Attributes oAuth2Attributes, Long userId);
	
	boolean isNicknameExist(String nickname);
	
	void checkEmailForJoin(String email);
	
	AccountDto getAccountById(Long userId);
	
	void checkEmailForUpdate(String email, Long userId);
	
	Long checkEmailForReset(String email);
	
	void updateNickname(NicknameDto nicknameDto, Long userId);
	
	void updateEmail(EmailAuthDto emailAuthDto, Long userId);
	
	void updateAddr(AddrDto addrDto, Long userId);
	
	void updateAgree(AgreeDto agreeDto, Long userId);
	
	void updatePassword(PasswordChangeDto passwordChangeDto, Long userId);
	
	void savePassword(PasswordSaveDto passwordSaveDto, Long userId);
	
	void resetPassword(String newPassword, Long userId);
	
	PrincipalDetails getUserDetailsByEmail(String email); // for authentication in UserDetailsService, OAuth2UserService
	
	PrincipalDetails getUserDetailsById(Long userId); // for JWT re-issuance after updating
	
	SnsInfo getSnsInfoByUserId(Long userId);
	
}
