package com.codingjoa.service;

public interface EmailService {
	
	//void sendAuthEmail(EmailDto emailDto);
	void sendAuthEmail(String memberEmail, String authCode);
	
}
