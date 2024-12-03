package com.codingjoa.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

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
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nextFireTime = context.getTrigger().getNextFireTime();
		log.info("\t > alarm will be triggered at {}", sdf.format(nextFireTime));
		
		schedulerService.sendAlarmMessage(message);
	}
	
}
