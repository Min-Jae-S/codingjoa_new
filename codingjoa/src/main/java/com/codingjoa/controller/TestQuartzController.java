package com.codingjoa.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.codingjoa.quartz.service.SchedulerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestQuartzController {
	
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
		return "test/quartz";
	}

	@ResponseBody
	@GetMapping("/quartz/config")
	public ResponseEntity<Object> config() throws SchedulerException {
		log.info("## quartz config");
		log.info("\t > schedulerFactoryBean = {}", schedulerFactoryBean);
		log.info("\t     - autoStartup   = {}", schedulerFactoryBean.isAutoStartup());
		log.info("\t     - running       = {}", schedulerFactoryBean.isRunning());
		log.info("\t > scheduler = {}", scheduler);
		log.info("\t     - inStandbyMode = {}", scheduler.isInStandbyMode());
		log.info("\t     - started       = {}", scheduler.isStarted());
		log.info("\t     - shutdown      = {}", scheduler.isShutdown());
		
		Set<String> jobs = scheduler.getJobKeys(GroupMatcher.anyJobGroup()).stream()
				.map(jobKey -> jobKey.getName())
				.collect(Collectors.toSet());
		Set<String> triggers = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup()).stream()
				.map(triggerKey -> triggerKey.getName())
				.collect(Collectors.toSet());
		log.info("\t     - jobs          = {}", jobs);
		log.info("\t     - triggers      = {}", triggers);
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/quartz/state")
	public ResponseEntity<Object> state() throws SchedulerException {
		log.info("## state");
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		log.info("\t > triggerKeys = {}", triggerKeys);
		
		Set<String> pausedJobs = new HashSet<>();
		Set<String> runningJobs = new HashSet<>();
		Set<String> unknownJobs = new HashSet<>();
		log.info("\t ==============================================");
		for (TriggerKey triggerKey : triggerKeys) {
			Trigger trigger = scheduler.getTrigger(triggerKey);
			String jobName = trigger.getJobKey().getName();
			TriggerState triggerState = scheduler.getTriggerState(triggerKey);
			log.info("\t > {} = {}", jobName, triggerState);
			
			if (triggerState == TriggerState.PAUSED) {
				pausedJobs.add(jobName);
			} else if (triggerState == TriggerState.NORMAL) {
				runningJobs.add(jobName);
			} else {
				unknownJobs.add(jobName);
			}
		}
		log.info("\t ==============================================");
		log.info("\t > PAUSED  Jobs = {}", pausedJobs);
		log.info("\t > RUNNING Jobs = {}", runningJobs);
		log.info("\t > UNKNOWN Jobs = {}", unknownJobs);
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
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
	@GetMapping("/quartz/start/job-quartz")
	public ResponseEntity<Object> startQuartzJob() throws SchedulerException {
		log.info("## startQuartzJob");
		String result = schedulerService.startQuartzJob();
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
	@GetMapping("/quartz/pause/job-quartz")
	public ResponseEntity<Object> pauseQuartzJob() throws SchedulerException {
		log.info("## pauseJobC");
		String result = schedulerService.pauseQuartzJob();
		log.info("\t > {}", result);
		return ResponseEntity.ok(result);
	}
	
}
