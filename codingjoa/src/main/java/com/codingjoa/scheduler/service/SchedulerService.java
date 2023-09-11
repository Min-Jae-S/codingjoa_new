package com.codingjoa.scheduler.service;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.scheduler.JobA;
import com.codingjoa.scheduler.JobB;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SchedulerService {
	
	@Autowired
	private Scheduler scheduler;
	
	public void startJobA() throws SchedulerException {
		JobKey jobKeyA = JobKey.jobKey("jobA", "myJob");
		log.info("\t > jobA exists? {}", scheduler.checkExists(jobKeyA));

		if (!scheduler.checkExists(jobKeyA)) {
			log.info("\t > make jobDetailA and triggerA --> schedule JobA");
			JobDetail jobDetailA = JobBuilder.newJob(JobA.class)
					.withIdentity(jobKeyA)
					.storeDurably()
					.build();
			
			SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(10)
					.repeatForever();
			Trigger triggerA = TriggerBuilder.newTrigger()
					.forJob(jobDetailA)
					.withIdentity("triggerA", "myTrigger")
					.withSchedule(scheduleBuilder)
					.build();
			scheduler.scheduleJob(jobDetailA, triggerA);
		} else {
			log.info("\t > resume JobA");
			scheduler.resumeJob(jobKeyA);
		}
	}
	
	public void startJobB() throws SchedulerException {
		JobKey jobKeyB = JobKey.jobKey("jobB", "myJob");
		log.info("\t > jobB exists? {}", scheduler.checkExists(jobKeyB));
		
		if (!scheduler.checkExists(jobKeyB)) {
			log.info("\t > make jobDetailB and triggerB --> schedule JobB");
			JobDetail jobDetailB = JobBuilder.newJob(JobB.class)
					.withIdentity(jobKeyB)
					.storeDurably()
					.build();
			
			SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(30)
					.repeatForever();
			Trigger triggerB = TriggerBuilder.newTrigger()
					.forJob(jobDetailB)
					.withIdentity("triggerB", "myTrigger")
					.withSchedule(scheduleBuilder)
					.build();
			scheduler.scheduleJob(jobDetailB, triggerB);
		} else {
			log.info("\t > resume JobB");
			scheduler.resumeJob(jobKeyB);
		}
	}

}
