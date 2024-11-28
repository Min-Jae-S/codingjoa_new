package com.codingjoa.service;

public interface SchedulerService {
	
	void execute();
	
	void insert(String jobName);
	
	void insertOnException(String jobName);
	
}
