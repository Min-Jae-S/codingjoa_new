package com.codingjoa.scheduler.service;

import javax.annotation.Resource;

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
	
	public Scheduler getSchedulerInstance() {
		return scheduler;
	}
	
	public boolean resumeJobA() throws SchedulerException {
		TriggerKey triggerKeyA = triggerA.getKey();
		log.info("\t > triggerKeyA = {}", triggerKeyA);
		
		if (isTriggerPaused(triggerKeyA)) {
			scheduler.resumeTrigger(triggerKeyA);
			return true;
		}
		
		return false;
	}
	
	public boolean resumeJobB() throws SchedulerException {
		TriggerKey triggerKeyB = triggerB.getKey();
		log.info("\t > triggerKeyB = {}", triggerKeyB);
		
		if (isTriggerPaused(triggerKeyB)) {
			scheduler.resumeTrigger(triggerKeyB);
			return true;
		}
		
		return false;
	}
	
	private boolean isTriggerPaused(TriggerKey triggerKey) throws SchedulerException {
		TriggerState state = scheduler.getTriggerState(triggerKey);
		return (state == TriggerState.PAUSED) ? true : false;
	}

}
