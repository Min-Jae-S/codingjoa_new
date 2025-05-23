package com.codingjoa.obsolete;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.codingjoa.service.SchedulerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JobB extends QuartzJobBean {
	
	private final SchedulerService schedulerService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}.executeInternal", this.getClass().getSimpleName());
		
		String jobName = context.getJobDetail().getKey().getName();
		log.info("\t > jobName = {}", jobName);
		
		schedulerService.insertOnException(jobName);
	}
	
}
