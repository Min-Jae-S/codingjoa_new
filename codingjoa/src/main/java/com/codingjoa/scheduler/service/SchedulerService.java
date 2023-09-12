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
	
	public void startJobA() throws SchedulerException {
		TriggerKey triggerKeyA = triggerA.getKey();
		if (!isTriggerPaused(triggerKeyA)) {
			log.info("\t > start JobA that was paused");
			scheduler.resumeTrigger(triggerKeyA);
		} else {
			log.info("\t > JobA is already running");
		}
	}
	
	public void startJobB() throws SchedulerException {
		TriggerKey triggerKeyB = triggerB.getKey();
		if (!isTriggerPaused(triggerKeyB)) {
			log.info("\t > start JobB that was paused");
			scheduler.resumeTrigger(triggerKeyB);
		} else {
			log.info("\t > JobB is already running");
		}
	}
	
	private boolean isTriggerPaused(TriggerKey triggerKey) throws SchedulerException {
		TriggerState state = scheduler.getTriggerState(triggerKey);
		return (state == TriggerState.PAUSED) ? true : false;
	}

}
