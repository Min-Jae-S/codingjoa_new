package com.codingjoa.scheduler.service;

import java.util.List;

import javax.annotation.Resource;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SchedulerService {
	
	@Autowired
	private Scheduler scheduler;
	
	@Resource(name = "triggerA")
	private Trigger triggerA;

	@Resource(name = "triggerB")
	private Trigger triggerB;

	@Resource(name = "jobDetailA")
	private JobDetail jobDetailA;
	
	@Resource(name = "jobDetailB")
	private JobDetail jobDetailB;
	
	public void state() throws SchedulerException {
		TriggerKey triggerKeyA = triggerA.getKey();
		log.info("\t > triggerA state = {}", scheduler.getTriggerState(triggerKeyA));
		
		TriggerKey triggerKeyB = triggerB.getKey();
		log.info("\t > triggerB state = {}", scheduler.getTriggerState(triggerKeyB));
	}
	
	public boolean resumeJobA() throws SchedulerException {
		JobKey jobKeyA = jobDetailA.getKey();
		log.info("\t > jobKeyA = {}", jobKeyA);
		
		if (isJobPaused(jobKeyA)) {
			scheduler.resumeJob(jobKeyA);
			return true;
		}
		
		return false;
	}
	
	public boolean resumeJobB() throws SchedulerException {
		JobKey jobKeyB = jobDetailB.getKey();
		log.info("\t > jobKeyB = {}", jobKeyB);
		
		if (isJobPaused(jobKeyB)) {
			scheduler.resumeJob(jobKeyB);
			return true;
		}
		
		return false;
	}
	
	private boolean isJobPaused(JobKey jobKey) throws SchedulerException {
		List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
		for (Trigger trigger: triggers) {
			TriggerState state = scheduler.getTriggerState(trigger.getKey());
			if (state == TriggerState.PAUSED) {
				return true;
			}
		}
		return false;
	}

}
