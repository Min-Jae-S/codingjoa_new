package com.codingjoa.service;

import com.codingjoa.enums.MailType;
import com.codingjoa.test.Sample;

public interface EmailService {
	
	void send(String to, MailType mailType, String value);
	
	void triggerAsyncEx(Sample sample, String param);
	
}
