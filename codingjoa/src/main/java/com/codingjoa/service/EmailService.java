package com.codingjoa.service;

import com.codingjoa.basic.test.Sample;
import com.codingjoa.enums.MailType;

public interface EmailService {
	
	void send(String to, MailType mailType, String value);
	
	void triggerAsyncEx(Sample sample, String param);
	
}
