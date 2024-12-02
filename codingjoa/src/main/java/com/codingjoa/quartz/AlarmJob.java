package com.codingjoa.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.codingjoa.service.SchedulerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AlarmJob implements Job {
	
	private final SchedulerService schedulerService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}.execute", this.getClass().getSimpleName());
		
		String message = context.getMergedJobDataMap().getString("message");
		log.info("\t > message = {}", message);
		
		schedulerService.sendAlarmMessage(message);
	}
	
}
