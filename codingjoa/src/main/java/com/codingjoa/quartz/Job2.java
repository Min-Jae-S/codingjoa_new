package com.codingjoa.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.codingjoa.obsolete.quartz.QuartzJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Job2 extends QuartzJob {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}.executeInternal", this.getClass().getSimpleName());
	}
	
}
