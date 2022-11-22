package com.codingjoa.service;

public interface EmailService {
	
	//public void sendAuthEmail(EmailDto emailDto);
	public void sendAuthEmail(String memberEmail, String authCode);
	
}
