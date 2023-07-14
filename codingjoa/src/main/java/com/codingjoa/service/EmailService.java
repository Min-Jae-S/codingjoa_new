package com.codingjoa.service;

public interface EmailService {
	
	//void sendAuthEmail(EmailDto emailDto);
	void sendAuthCode(String memberEmail, String authCode);
	
	void sendFoundAccount(String memberEmail, String memberId);
	
}
