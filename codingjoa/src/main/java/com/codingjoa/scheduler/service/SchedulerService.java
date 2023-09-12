package com.codingjoa.scheduler.service;

import javax.annotation.Resource;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
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

	@Resource(name = "triggerA")
	private Trigger triggerB;
	
	public void startJobA() throws SchedulerException {
		TriggerKey triggerKeyA = triggerA.getKey();
		log.info("\t > triggerKeyA exists ? {}", scheduler.checkExists(triggerKeyA));
		
		if (scheduler.checkExists(triggerKeyA)) {
			scheduler.resumeTrigger(triggerKeyA);
			log.info("\t > resume triggerA");
		}
	}
	
	public void startJobB() throws SchedulerException {
		TriggerKey triggerKeyB = triggerB.getKey();
		log.info("\t > triggerKeyB exists ? {}", scheduler.checkExists(triggerKeyB));
		
		if (scheduler.checkExists(triggerKeyB)) {
			scheduler.resumeTrigger(triggerKeyB);
			log.info("\t > resume triggerB");
		}
	}

}
