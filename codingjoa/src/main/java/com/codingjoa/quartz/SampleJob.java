package com.codingjoa.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.codingjoa.service.SchedulerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleJob implements Job {
	
	@Autowired
	private SchedulerService schedulerService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}.executeInternal", this.getClass().getSimpleName());
		log.info("\t > ctx = {}", context);
		schedulerService.execute();
	}
	
}
