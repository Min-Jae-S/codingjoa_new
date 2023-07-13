package com.codingjoa.service;

public interface EmailService {
	
	//void sendAuthEmail(EmailDto emailDto);
	void sendAuthEmail(String memberEmail, String authCode);
	
	void sendFoundAccountEmail(String memberEmail, String memberId);
	
}
