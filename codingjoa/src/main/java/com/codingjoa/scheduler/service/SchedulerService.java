package com.codingjoa.scheduler.service;

import java.util.Set;

import javax.annotation.Resource;

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
	
	public void resumeAllJobs() throws SchedulerException {
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		for (TriggerKey triggerKey : triggerKeys) {
			if (isTriggerPaused(triggerKey)) {
				log.info("\t > resume paused {}", scheduler.getTrigger(triggerKey).getJobKey().getName());
				scheduler.resumeTrigger(triggerKey);
			}
		}
	}
	
	public boolean resumeJobA() throws SchedulerException {
		TriggerKey triggerKeyA = triggerA.getKey();
		if (isTriggerPaused(triggerKeyA)) {
			log.info("\t > resume paused JobA");
			scheduler.resumeTrigger(triggerKeyA);
			return true;
		}
		
		log.info("\t > JobA is already running");
		return false;
	}
	
	public boolean resumeJobB() throws SchedulerException {
		TriggerKey triggerKeyB = triggerB.getKey();
		if (isTriggerPaused(triggerKeyB)) {
			log.info("\t > resume paused JobB");
			scheduler.resumeTrigger(triggerKeyB);
			return true;
		}
		
		log.info("\t > JobB is already running");
		return false;
	}
	
	public void pauseAllJobs() throws SchedulerException {
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		for (TriggerKey triggerKey : triggerKeys) {
			if (!isTriggerPaused(triggerKey)) {
				log.info("\t > pause running {}", scheduler.getTrigger(triggerKey).getJobKey().getName());
				scheduler.pauseTrigger(triggerKey);
			}
		}
	}
	
	public boolean pauseJobA() throws SchedulerException {
		TriggerKey triggerKeyA = triggerA.getKey();
		if (!isTriggerPaused(triggerKeyA)) {
			log.info("\t > pause running JobA");
			scheduler.pauseTrigger(triggerKeyA);
			return true;
		}
		
		log.info("\t > JobA is already paused");
		return false;
	}

	public boolean pauseJobB() throws SchedulerException {
		TriggerKey triggerKeyB = triggerB.getKey();
		if (!isTriggerPaused(triggerKeyB)) {
			log.info("\t > pause running JobB");
			scheduler.pauseTrigger(triggerKeyB);
			return true;
		}
		
		log.info("\t > JobB is already paused");
		return false;
	}
	
	private boolean isTriggerPaused(TriggerKey triggerKey) throws SchedulerException {
		TriggerState triggerState = scheduler.getTriggerState(triggerKey);
		return (triggerState == TriggerState.PAUSED) ? true : false;
	}

}
