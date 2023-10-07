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

	@Resource(name = "triggerC")
	private Trigger triggerC;
	
	public void startAllJobs() throws SchedulerException {
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		for (TriggerKey triggerKey : triggerKeys) {
			String jobName = scheduler.getTrigger(triggerKey).getJobKey().getName();
			if (isTriggerPaused(triggerKey)) {
				log.info("\t > resume paused {}", jobName);
				scheduler.resumeTrigger(triggerKey);
			} else {
				log.info("\t > {} is already running", jobName);
			}
		}
	}
	
	public String startJobA() throws SchedulerException {
		TriggerKey triggerKeyA = triggerA.getKey();
		if (isTriggerPaused(triggerKeyA)) {
			scheduler.resumeTrigger(triggerKeyA);
			return "resume paused JobA";
		}
		return "JobA is already running";
	}
	
	public String startJobB() throws SchedulerException {
		TriggerKey triggerKeyB = triggerB.getKey();
		if (isTriggerPaused(triggerKeyB)) {
			scheduler.resumeTrigger(triggerKeyB);
			return "resume paused JobB";
		}
		return "JobB is already running";
	}

	public String startJobC() throws SchedulerException {
		TriggerKey triggerKeyC = triggerC.getKey();
		if (isTriggerPaused(triggerKeyC)) {
			scheduler.resumeTrigger(triggerKeyC);
			return "resume paused JobC";
		}
		return "JobC is already running";
	}
	
	public void pauseAllJobs() throws SchedulerException {
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		for (TriggerKey triggerKey : triggerKeys) {
			String jobName = scheduler.getTrigger(triggerKey).getJobKey().getName();
			if (!isTriggerPaused(triggerKey)) {
				log.info("\t > pause running {}", jobName);
				scheduler.pauseTrigger(triggerKey);
			} else {
				log.info("\t > {} is already paused", jobName);
			}
		}
	}
	
	public String pauseJobA() throws SchedulerException {
		TriggerKey triggerKeyA = triggerA.getKey();
		if (!isTriggerPaused(triggerKeyA)) {
			scheduler.pauseTrigger(triggerKeyA);
			return "pause running JobA";
		}
		return "JobA is already paused";
	}

	public String pauseJobB() throws SchedulerException {
		TriggerKey triggerKeyB = triggerB.getKey();
		if (!isTriggerPaused(triggerKeyB)) {
			scheduler.pauseTrigger(triggerKeyB);
			return "pause running JobB";
		}
		return "JobB is already paused";
	}

	public String pauseJobC() throws SchedulerException {
		TriggerKey triggerKeyC = triggerC.getKey();
		if (!isTriggerPaused(triggerKeyC)) {
			scheduler.pauseTrigger(triggerKeyC);
			return "pause running JobC";
		}
		return "JobC is already paused";
	}
	
	private boolean isTriggerPaused(TriggerKey triggerKey) throws SchedulerException {
		TriggerState triggerState = scheduler.getTriggerState(triggerKey);
		return (triggerState == TriggerState.PAUSED) ? true : false;
	}
}
