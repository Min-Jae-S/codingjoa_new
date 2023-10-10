package com.codingjoa.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GlobalTriggerListener implements TriggerListener {
	
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.info("## triggerFired at {}", trigger.getStartTime());
        log.info("\t > triggerKey = {}", trigger.getKey());
        log.info("\t > jobKey = {}", trigger.getJobKey());
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		log.info("## triggerMisfired at {}", trigger.getStartTime());
		log.info("\t > triggerKey = {}", trigger.getKey());
        log.info("\t > jobKey = {}", trigger.getJobKey());
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		log.info("## triggerComplete at {}", trigger.getStartTime());
		log.info("\t > triggerKey = {}", trigger.getKey());
        log.info("\t > jobKey = {}", trigger.getJobKey());
	}

}
