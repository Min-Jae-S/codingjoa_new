package com.codingjoa.batch;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CountSyncProgressListener {
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
	}
	
	
	@AfterStep
	public void afterStep(StepExecution stepExecution) {
		
	}
	
}
