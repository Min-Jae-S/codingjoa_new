package com.codingjoa.service;

import java.util.List;

import com.codingjoa.test.TestSchedulerData;

public interface SchedulerService {
	
	void execute();
	
	void insert(String jobName);

	void insert(String jobName, String id);
	
	void insertOnException(String jobName);
	
	List<TestSchedulerData> getSamples();
	
	void deleteSamples();
	
	void sendAlarmMessage(String message);
	
}
