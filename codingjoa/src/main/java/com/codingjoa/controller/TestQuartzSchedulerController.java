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
		log.info("\t > autoStartup        = {}", schedulerFactoryBean.isAutoStartup());
		log.info("\t > running            = {}", schedulerFactoryBean.isRunning());
		log.info("\t > inStandbyMode      = {}", scheduler.isInStandbyMode());
		log.info("\t > started            = {}", scheduler.isStarted());
		log.info("\t > shutdown           = {}", scheduler.isShutdown());
		log.info("\t > registerd jobs     = {}", scheduler.getJobKeys(GroupMatcher.anyJobGroup()));
		
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		log.info("\t > registerd triggers = {}", triggerKeys);
		
		Set<JobKey> pausedJobs = new HashSet<>();
		for (TriggerKey triggerKey : triggerKeys) {
			if (scheduler.getTriggerState(triggerKey) == TriggerState.PAUSED) {
				 Trigger pausedTrigger = scheduler.getTrigger(triggerKey);
				 JobKey pausedJobKey = pausedTrigger.getJobKey();
				 pausedJobs.add(pausedJobKey);
			}
		}
		log.info("\t > paused jobs        = {}", pausedJobs);
		
		return ResponseEntity.ok("config success");
	}
	
	@ResponseBody
	@GetMapping("/quartz/resume")
	public ResponseEntity<Object> resumeAllJobs() throws SchedulerException {
		log.info("## resumeAllJobs");
		schedulerService.resumeAllJobs();
		return ResponseEntity.ok("resume all jobs");
	}

	@ResponseBody
	@GetMapping("/quartz/resume/job-a")
	public ResponseEntity<Object> resumeJobA() throws SchedulerException {
		log.info("## resumeJobA");
		String msg = (schedulerService.resumeJobA() == true) ? "resume paused JobA" : "JobA is already running";
		return ResponseEntity.ok(msg);
	}

	@ResponseBody
	@GetMapping("/quartz/resume/job-b")
	public ResponseEntity<Object> resumeJobB() throws SchedulerException {
		log.info("## resumeJobB");
		String msg = (schedulerService.resumeJobB() == true) ? "resume paused JobB" : "JobB is already running";
		return ResponseEntity.ok(msg);
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
		String msg = (schedulerService.pauseJobA() == true) ? "pause running JobA" : "JobA is already paused";
		return ResponseEntity.ok(msg);
	}

	@ResponseBody
	@GetMapping("/quartz/pause/job-b")
	public ResponseEntity<Object> pauseJobB() throws SchedulerException {
		log.info("## pauseJobB");
		String msg = (schedulerService.pauseJobB() == true) ? "pause running JobB" : "JobB is already paused";
		return ResponseEntity.ok(msg);
	}
	
}
