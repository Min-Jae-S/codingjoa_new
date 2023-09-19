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
		log.info("\t > registerd triggers = {}", scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup()));
		return ResponseEntity.ok("config success");
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
		return ResponseEntity.ok(schedulerService.resumeJobA());
	}

	@ResponseBody
	@GetMapping("/quartz/resume/job-b")
	public ResponseEntity<Object> resumeJobB() throws SchedulerException {
		log.info("## resumeJobB");
		return ResponseEntity.ok(schedulerService.resumeJobB());
	}

	@ResponseBody
	@GetMapping("/quartz/resume/job-c")
	public ResponseEntity<Object> resumeJobC() throws SchedulerException {
		log.info("## resumeJobC");
		return ResponseEntity.ok(schedulerService.resumeJobC());
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
		return ResponseEntity.ok(schedulerService.pauseJobA());
	}

	@ResponseBody
	@GetMapping("/quartz/pause/job-b")
	public ResponseEntity<Object> pauseJobB() throws SchedulerException {
		log.info("## pauseJobB");
		return ResponseEntity.ok(schedulerService.pauseJobB());
	}

	@ResponseBody
	@GetMapping("/quartz/pause/job-c")
	public ResponseEntity<Object> pauseJobC() throws SchedulerException {
		log.info("## pauseJobC");
		return ResponseEntity.ok(schedulerService.pauseJobC());
	}
	
}
