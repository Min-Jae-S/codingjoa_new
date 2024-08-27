package com.codingjoa.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GlobalJobListener implements JobListener {
	
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
        log.info("## jobToBeExecuted");
        log.info("\t > jobKey = {}", context.getJobDetail().getKey());
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
        log.info("## jobExecutionVetoed");
        log.info("\t > jobKey = {}", context.getJobDetail().getKey());
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.info("## jobWasExecuted");
        log.info("\t > jobKey = {}", context.getJobDetail().getKey());
	}

}
