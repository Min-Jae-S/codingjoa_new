package com.codingjoa.controller.test;

import java.util.HashSet;
import java.util.Set;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.config.QuartzConfig;
import com.codingjoa.quartz.service.SchedulerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestQuartzController {
	
	@Autowired
	private Scheduler scheduler;
	
	@Autowired
	private SchedulerService schedulerService;
	
	@Autowired
	private QuartzConfig quartzConfig;
	
	@GetMapping("/quartz/config")
	public ResponseEntity<Object> config() throws SchedulerException {
		log.info("## quartz config");
		quartzConfig.printQuartzConfig();
		return ResponseEntity.ok("success");
	}

	@GetMapping("/quartz/state")
	public ResponseEntity<Object> state() throws SchedulerException {
		log.info("## state");
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		log.info("\t > triggerKeys = {}", triggerKeys);
		
		Set<String> pausedJobs = new HashSet<>();
		Set<String> runningJobs = new HashSet<>();
		Set<String> unknownJobs = new HashSet<>();
		log.info("\t ====================================================================================");
		for (TriggerKey triggerKey : triggerKeys) {
			Trigger trigger = scheduler.getTrigger(triggerKey);
			String jobName = trigger.getJobKey().getName();
			TriggerState triggerState = scheduler.getTriggerState(triggerKey);
			log.info("\t > '{}' = {}", jobName, triggerState);
			
			if (triggerState == TriggerState.PAUSED) {
				pausedJobs.add(jobName);
			} else if (triggerState == TriggerState.NORMAL) {
				runningJobs.add(jobName);
			} else {
				unknownJobs.add(jobName);
			}
		}
		log.info("\t ====================================================================================");
		log.info("\t > PAUSED  Jobs = {}", pausedJobs);
		log.info("\t > RUNNING Jobs = {}", runningJobs);
		log.info("\t > UNKNOWN Jobs = {}", unknownJobs);
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/quartz/paused-jobs")
	public ResponseEntity<Object> pausedJobs() throws SchedulerException {
		log.info("## pausedJobs");
		Set<JobKey> pausedJobs = new HashSet<>();
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		for (TriggerKey triggerKey : triggerKeys) {
			TriggerState triggerState = scheduler.getTriggerState(triggerKey);
			if (triggerState == TriggerState.PAUSED) {
				 Trigger pausedTrigger = scheduler.getTrigger(triggerKey);
				 JobKey pausedJobKey = pausedTrigger.getJobKey();
				 pausedJobs.add(pausedJobKey);
			}
		}
		log.info("\t > paused jobs = {}", pausedJobs);
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/quartz/start")
	public ResponseEntity<Object> startAllJobs() throws SchedulerException {
		log.info("## startAllJobs");
		schedulerService.startAllJobs();
		return ResponseEntity.ok("start all jobs");
	}

	@GetMapping("/quartz/start/job-a")
	public ResponseEntity<Object> startJobA() throws SchedulerException {
		log.info("## startJobA");
		String result = schedulerService.startJobA();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/quartz/start/job-b")
	public ResponseEntity<Object> startJobB() throws SchedulerException {
		log.info("## startJobB");
		String result = schedulerService.startJobB();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/quartz/start/job-quartz")
	public ResponseEntity<Object> startQuartzJob() throws SchedulerException {
		log.info("## startQuartzJob");
		String result = schedulerService.startQuartzJob();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/quartz/pause")
	public ResponseEntity<Object> pauseAllJobs() throws SchedulerException {
		log.info("## pauseAllJobs");
		schedulerService.pauseAllJobs();
		return ResponseEntity.ok("pause all jobs");
	}

	@GetMapping("/quartz/pause/job-a")
	public ResponseEntity<Object> pauseJobA() throws SchedulerException {
		log.info("## pauseJobA");
		String result = schedulerService.pauseJobA();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/quartz/pause/job-b")
	public ResponseEntity<Object> pauseJobB() throws SchedulerException {
		log.info("## pauseJobB");
		String result = schedulerService.pauseJobB();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/quartz/pause/job-quartz")
	public ResponseEntity<Object> pauseQuartzJob() throws SchedulerException {
		log.info("## pauseJobC");
		String result = schedulerService.pauseQuartzJob();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}
	
}
