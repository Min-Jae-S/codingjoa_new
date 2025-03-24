package com.codingjoa.service;

import com.codingjoa.dto.AccountDto;
import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordSaveDto;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.oauth2.OAuth2Attributes;

public interface UserService {

	void saveUser(JoinDto joinDto);
	
	void saveOAuth2User(OAuth2Attributes oAuth2Attributes);
	
	void connectOAuth2User(OAuth2Attributes oAuth2Attributes, Long userId);
	
	boolean isNicknameExist(String nickname);
	
	void checkEmailForJoin(String email);
	
	AccountDto getAccountById(Long userId);
	
	void checkEmailForUpdate(String email, long userId);
	
	Long checkEmailForReset(String email);
	
	void updateNickname(NicknameDto nicknameDto, long userId);
	
	void updateEmail(EmailAuthDto emailAuthDto, long userId);
	
	void updateAddr(AddrDto addrDto, long userId);
	
	void updateAgree(AgreeDto agreeDto, long userId);
	
	void updatePassword(PasswordChangeDto passwordChangeDto, long userId);
	
	void savePassword(PasswordSaveDto passwordSaveDto, long userId);
	
	void resetPassword(String newPassword, Long userId);
	
	PrincipalDetails getUserDetailsByEmail(String email); // for authentication in UserDetailsService, OAuth2UserService
	
	PrincipalDetails getUserDetailsById(long userId); // for JWT re-issuance after updating
	
}
