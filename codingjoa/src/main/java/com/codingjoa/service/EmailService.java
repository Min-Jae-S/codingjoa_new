package com.codingjoa.service;

public interface EmailService {
	
	void sendAuthCode(String email, String authCode);
	
	void sendResetPasswordUrl(String email, String resetPasswordUrl);
	
}
