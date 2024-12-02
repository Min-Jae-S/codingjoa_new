package com.codingjoa.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.codingjoa.service.SchedulerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JobC implements Job {
	
	private final SchedulerService schedulerService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}.execute", this.getClass().getSimpleName());
		
		String jobName = context.getJobDetail().getKey().getName();
		log.info("\t > jobName = {}", jobName);
		
		String id = context.getMergedJobDataMap().getString("id");
		log.info("\t > id = {}", id);
		
		schedulerService.insert(jobName, id);
	}
	
}
