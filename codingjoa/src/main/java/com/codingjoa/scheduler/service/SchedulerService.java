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
	
	public void state() throws SchedulerException {
		log.info("\t > triggerA state = {}", scheduler.getTriggerState(triggerA.getKey()));
		log.info("\t > triggerB state = {}", scheduler.getTriggerState(triggerB.getKey()));
	}
	
	public void resumeAllJobs() throws SchedulerException {
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		for (TriggerKey triggerKey : triggerKeys) {
			if (isTriggerPaused(triggerKey)) {
				scheduler.resumeTrigger(triggerKey);
			}
		}
	}
	
	public boolean resumeJobA() throws SchedulerException {
		TriggerKey triggerKeyA = triggerA.getKey();
		if (isTriggerPaused(triggerKeyA)) {
			scheduler.resumeTrigger(triggerKeyA);
			return true;
		}
		return false;
	}
	
	public boolean resumeJobB() throws SchedulerException {
		TriggerKey triggerKeyB = triggerB.getKey();
		if (isTriggerPaused(triggerKeyB)) {
			scheduler.resumeTrigger(triggerKeyB);
			return true;
		}
		return false;
	}
	
	public void pauseAllJobs() throws SchedulerException {
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		for (TriggerKey triggerKey : triggerKeys) {
			if (!isTriggerPaused(triggerKey)) {
				scheduler.pauseTrigger(triggerKey);
			}
		}
	}
	
	private boolean isTriggerPaused(TriggerKey triggerKey) throws SchedulerException {
		TriggerState triggerState = scheduler.getTriggerState(triggerKey);
		return (triggerState == TriggerState.PAUSED) ? true : false;
	}

}
