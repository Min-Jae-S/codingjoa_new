package com.codingjoa.controller;

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
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.scheduler.service.SchedulerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/scheduler")
@Controller
public class TestQuartzSchedulerController {
	
	/*	
	 * ## Quartz
	 * 	> enable in-memory job scheduler
	 * 	> clustering using database
	 * 	> Job, Trigger
	 * 	> JobDetails, JobDataMap, JobListener, TriggerListener
	 * 	> start (scheduleJob, resumeJob, triggerJob, addJob)
	 * 	> stop (interrupt, unscheduleJob, pauseJob, deleteJob)
	 */
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	private Scheduler scheduler;
	
	@Autowired
	private SchedulerService schedulerService;
	
	@GetMapping("/quartz")
	public String main() {
		log.info("## quartz main");
		return "test/scheduler-quartz";
	}

	@ResponseBody
	@GetMapping("/quartz/config")
	public ResponseEntity<Object> config() throws SchedulerException {
		log.info("## config");
		log.info("\t > autoStartup   = {}", schedulerFactoryBean.isAutoStartup());
		log.info("\t > running       = {}", schedulerFactoryBean.isRunning());
		log.info("\t > inStandbyMode = {}", scheduler.isInStandbyMode());
		log.info("\t > started       = {}", scheduler.isStarted());
		log.info("\t > shutdown      = {}", scheduler.isShutdown());
		log.info("\t > jobs          = {}", scheduler.getJobKeys(GroupMatcher.anyJobGroup()));
		log.info("\t > triggers      = {}", scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup()));
		
		return ResponseEntity.ok("config success");
	}

	@ResponseBody
	@GetMapping("/quartz/status")
	public ResponseEntity<Object> status() throws SchedulerException {
		log.info("## status");
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		log.info("\t > triggerKeys = {}", triggerKeys);
		
		for (TriggerKey triggerKey : triggerKeys) {
			String jobName = scheduler.getTrigger(triggerKey).getJobKey().getName();
			log.info("\t > {} : {}", jobName, scheduler.getTriggerState(triggerKey));
		}
		
		return ResponseEntity.ok("status success");
	}
	
	@ResponseBody
	@GetMapping("/quartz/paused-jobs")
	public ResponseEntity<Object> pausedJobs() throws SchedulerException {
		log.info("## pausedJobs");
		Set<JobKey> pausedJobs = new HashSet<>();
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		for (TriggerKey triggerKey : triggerKeys) {
			if (scheduler.getTriggerState(triggerKey) == TriggerState.PAUSED) {
				 Trigger pausedTrigger = scheduler.getTrigger(triggerKey);
				 JobKey pausedJobKey = pausedTrigger.getJobKey();
				 pausedJobs.add(pausedJobKey);
			}
		}
		
		log.info("\t > paused jobs = {}", pausedJobs);
		return ResponseEntity.ok("pausedJobs success");
	}
	
	@ResponseBody
	@GetMapping("/quartz/start")
	public ResponseEntity<Object> startAllJobs() throws SchedulerException {
		log.info("## startAllJobs");
		schedulerService.startAllJobs();
		return ResponseEntity.ok("start all jobs");
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-a")
	public ResponseEntity<Object> startJobA() throws SchedulerException {
		log.info("## startJobA");
		String result = schedulerService.startJobA();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-b")
	public ResponseEntity<Object> startJobB() throws SchedulerException {
		log.info("## startJobB");
		String result = schedulerService.startJobB();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-c")
	public ResponseEntity<Object> startJobC() throws SchedulerException {
		log.info("## startJobC");
		String result = schedulerService.startJobC();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}

	@ResponseBody
	@GetMapping("/quartz/pause")
	public ResponseEntity<Object> pauseAllJobs() throws SchedulerException {
		log.info("## pauseAllJobs");
		schedulerService.pauseAllJobs();
		return ResponseEntity.ok("pause all jobs");
	}

	@ResponseBody
	@GetMapping("/quartz/pause/job-a")
	public ResponseEntity<Object> pauseJobA() throws SchedulerException {
		log.info("## pauseJobA");
		String result = schedulerService.pauseJobA();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}

	@ResponseBody
	@GetMapping("/quartz/pause/job-b")
	public ResponseEntity<Object> pauseJobB() throws SchedulerException {
		log.info("## pauseJobB");
		String result = schedulerService.pauseJobB();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}

	@ResponseBody
	@GetMapping("/quartz/pause/job-c")
	public ResponseEntity<Object> pauseJobC() throws SchedulerException {
		log.info("## pauseJobC");
		String result = schedulerService.pauseJobC();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}
	
}
