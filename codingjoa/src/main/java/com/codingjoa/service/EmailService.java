package com.codingjoa.service;

public interface EmailService {
	
	void sendAuthCode(String email, String authCode);
	
	void sendFoundAccount(String memberEmail, String memberId);
	
	void sendResetPasswordUrl(String memberEmail, String memberId, String resetPasswordUrl);
	
}
