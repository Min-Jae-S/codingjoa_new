package com.codingjoa.service;

import com.codingjoa.enumclass.MailType;

public interface EmailService {
	
	void send(String to, MailType mailType, String value);
	
}
