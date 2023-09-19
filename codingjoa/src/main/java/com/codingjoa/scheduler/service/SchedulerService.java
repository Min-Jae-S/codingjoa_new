package com.codingjoa.scheduler.service;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
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

	@Resource(name = "triggerC")
	private Trigger triggerC;
	
	@Resource(name = "jobDetailA")
	private JobDetail jobDetailA;
	
	@Resource(name = "jobDetailB")
	private JobDetail jobDetailB;
	
	@Resource(name = "jobDetailC")
	private JobDetail jobDetailC;
	
	public void startAllJobs() throws SchedulerException {
		List<Trigger> triggers = List.of(triggerA, triggerB, triggerC);
		for (Trigger trigger : triggers) {
			TriggerKey triggerKey = trigger.getKey();
			TriggerState triggerState = scheduler.getTriggerState(triggerKey);
			if (triggerState == TriggerState.NONE) {
				log.info("\t > start {} newly", trigger.getJobKey().getName());
			} else if (triggerState == TriggerState.PAUSED) {
				scheduler.resumeTrigger(triggerKey);
				log.info("\t > resume paused {}", trigger.getJobKey().getName());
			}
		}
	}
	
	public String startJobA() throws SchedulerException {
		TriggerKey triggerKeyA = triggerA.getKey();
		TriggerState triggerState = scheduler.getTriggerState(triggerKeyA);
		log.info("\t > trigger = {}", triggerKeyA);
		log.info("\t > trigger state = {}", triggerState);
		
		if (triggerState == TriggerState.NONE) {
			scheduler.scheduleJob(jobDetailA, triggerA);
			return "start JobA newly";
		}
		
		if (triggerState == TriggerState.PAUSED) {
			scheduler.resumeTrigger(triggerKeyA);
			return "resume paused JobA";
		}
		
		return "JobA is already running";
	}
	
	public String startJobB() throws SchedulerException {
		TriggerKey triggerKeyB = triggerB.getKey();
		TriggerState triggerState = scheduler.getTriggerState(triggerKeyB);
		log.info("\t > trigger = {}", triggerKeyB);
		log.info("\t > trigger state = {}", triggerState);
		
		if (triggerState == TriggerState.NONE) {
			scheduler.scheduleJob(jobDetailB, triggerB);
			return "start JobB newly";
		}
		
		if (triggerState == TriggerState.PAUSED) {
			scheduler.resumeTrigger(triggerKeyB);
			return "resume paused JobB";
		}
		
		return "JobB is already running";
	}

	public String startJobC() throws SchedulerException {
		TriggerKey triggerKeyC = triggerC.getKey();
		TriggerState triggerState = scheduler.getTriggerState(triggerKeyC);
		log.info("\t > trigger = {}", triggerKeyC);
		log.info("\t > trigger state = {}", triggerState);
		
		if (triggerState == TriggerState.NONE) {
			scheduler.scheduleJob(jobDetailC, triggerC);
			return "start JobC newly";
		}
		
		if (triggerState == TriggerState.PAUSED) {
			scheduler.resumeTrigger(triggerKeyC);
			return "resume paused JobC";
		}
		
		return "JobC is already running";
		
//		TriggerKey triggerKeyC = triggerC.getKey();
//		if (isTriggerPaused(triggerKeyC)) {
//			scheduler.resumeTrigger(triggerKeyC);
//			return "resume paused JobC";
//		}
//		return "JobC is already running";
	}
	
	public void pauseAllJobs() throws SchedulerException {
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		log.info("\t > triggerKeys = {}", triggerKeys);
		for (TriggerKey triggerKey : triggerKeys) {
			if (!isTriggerPaused(triggerKey)) {
				log.info("\t > pause running {}", scheduler.getTrigger(triggerKey).getJobKey().getName());
				scheduler.pauseTrigger(triggerKey);
			}
		}
	}
	
	public String pauseJobA() throws SchedulerException {
		TriggerKey triggerKeyA = triggerA.getKey();
		TriggerState triggerState = scheduler.getTriggerState(triggerKeyA);
		log.info("\t > trigger = {}", triggerKeyA);
		log.info("\t > trigger state = {}", triggerState);
		
		if (triggerState == TriggerState.NONE) {
			return "JobA not exist";
		}
		
		if (triggerState == TriggerState.PAUSED) {
			return "JobA is already paused";
		}
		
		scheduler.pauseTrigger(triggerKeyA);
		return "pause running JobA";
	}

	public String pauseJobB() throws SchedulerException {
		TriggerKey triggerKeyB = triggerB.getKey();
		TriggerState triggerState = scheduler.getTriggerState(triggerKeyB);
		log.info("\t > trigger = {}", triggerKeyB);
		log.info("\t > trigger state = {}", triggerState);
		
		if (triggerState == TriggerState.NONE) {
			return "JobB not exist";
		}
		
		if (triggerState == TriggerState.PAUSED) {
			return "JobB is already paused";
		}
		
		scheduler.pauseTrigger(triggerKeyB);
		return "pause running JobB";
	}

	public String pauseJobC() throws SchedulerException {
		TriggerKey triggerKeyC = triggerC.getKey();
		TriggerState triggerState = scheduler.getTriggerState(triggerKeyC);
		log.info("\t > trigger = {}", triggerKeyC);
		log.info("\t > trigger state = {}", triggerState);
		
		if (triggerState == TriggerState.NONE) {
			return "JobC not exist";
		}
		
		if (triggerState == TriggerState.PAUSED) {
			return "JobC is already paused";
		}
		
		scheduler.pauseTrigger(triggerKeyC);
		return "pause running JobC";
		
//		TriggerKey triggerKeyC = triggerC.getKey();
//		if (!isTriggerPaused(triggerKeyC)) {
//			scheduler.pauseTrigger(triggerKeyC);
//			return "pause running JobC";
//		}
//		return "JobC is already paused";
	}
	
	private boolean isTriggerPaused(TriggerKey triggerKey) throws SchedulerException {
		TriggerState triggerState = scheduler.getTriggerState(triggerKey);
		return (triggerState == TriggerState.PAUSED) ? true : false;
	}

}
