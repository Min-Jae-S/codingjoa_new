package com.codingjoa.service;

import com.codingjoa.enumclass.MailType;

public interface EmailService {
	
//	void sendAuthCode(String to, String authCode);
//	
//	void sendPasswordResetLink(String to, String passwordResetLink);
	
	void send(String to, MailType mailType, String value);
	
}
