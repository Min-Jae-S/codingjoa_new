package com.codingjoa.service;

import javax.mail.MessagingException;

import com.codingjoa.enums.MailType;
import com.codingjoa.test.Sample;

public interface EmailService {
	
	void send(String to, MailType mailType, String value) throws MessagingException;
	
	void triggerAsyncEx(Sample sample, String param);
	
}
