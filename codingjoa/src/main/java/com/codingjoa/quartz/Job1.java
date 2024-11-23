package com.codingjoa.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.codingjoa.service.SchedulerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Job1 implements Job {
	
	private final SchedulerService schedulerService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}.executeInternal", this.getClass().getSimpleName());
		schedulerService.execute();
	}
	
}
