package com.codingjoa.service;

import java.util.List;

import com.codingjoa.test.TestSchedulerData;

public interface SchedulerService {
	
	void execute();
	
	void insert(String jobName);
	
	void insertOnException(String jobName);
	
	List<TestSchedulerData> getSampleData();
	
	void deleteSampleData();
	
}
