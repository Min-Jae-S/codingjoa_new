package com.codingjoa.service;

import com.codingjoa.dto.EmailDto;

public interface EmailService {
	
	public void sendAuthEmail(EmailDto emailDto);
	
}
