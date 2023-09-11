package com.codingjoa.controller;

import java.util.Set;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/scheduler")
@Controller
public class TestQuartzSchedulerController {
	
	/*	
	 * ## Quartz
	 * 	> enable in-memory job scheduler
	 * 	> clustering using database
	 * 	> Scheduler, Job, Trigger
	 * 	> JobDetails, JobDataMap, JobListener, TriggerListener
	 */
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	private Scheduler scheduler;
	
	@SuppressWarnings("unused")
	private void loggingJobAndTrigger(Scheduler scheduler) throws SchedulerException {
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
//		Map<String, Object> jobsAndTriggers = new HashMap<>();
//		for (JobKey jobKey : jobKeys) {
//			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
//			List<String> triggerNames = triggers
//					.stream()
//					.map(trigger -> trigger.getKey().getName())
//					.collect(Collectors.toList());
//			jobsAndTriggers.put(jobKey.getName(), triggerNames);
//		}
//		log.info("\t > jobs & triggers");
//		log.info("\t > jobs = {}", jobsAndTriggers.keySet());
//		log.info("\t > triggers = {}", jobsAndTriggers.values());
	}
	
	@GetMapping("/quartz")
	public String main() {
		log.info("## main");
		return "test/scheduler-quartz";
	}

	@ResponseBody
	@GetMapping("/quartz/config")
	public  ResponseEntity<Object> config() throws SchedulerException {
		log.info("## config");
		log.info("\t > isAutoStartup = {}", schedulerFactoryBean.isAutoStartup());
		log.info("\t > isRunning = {}", schedulerFactoryBean.isRunning());
		log.info("\t > scheduler = {}", scheduler);
		log.info("\t\t - schedulerName = {}", scheduler.getSchedulerName());
		log.info("\t\t - isInStandbyMode = {}", scheduler.isInStandbyMode());
		log.info("\t\t - isStarted = {}", scheduler.isStarted());
		log.info("\t\t - isShutdown = {}", scheduler.isShutdown());
		
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
		log.info("\t > jobKeys = {}", jobKeys);
		
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		log.info("\t > triggerKeys = {}", triggerKeys);
		
		return ResponseEntity.ok("config SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/start")
	public ResponseEntity<Object> startAllJobs() throws SchedulerException {
		log.info("## startAllJobs");
		scheduler.start();
		return ResponseEntity.ok("startAllJobs SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-a")
	public ResponseEntity<Object> startJobA() throws SchedulerException {
		log.info("## startJobA");
		JobKey jobKeyA = JobKey.jobKey("jobA");
		log.info("\t > jobKeyA = {}", jobKeyA);
		return ResponseEntity.ok("startJobA SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-b")
	public ResponseEntity<Object> startJobB() throws SchedulerException {
		log.info("## startJobB");
		JobKey jobKeyB = JobKey.jobKey("jobB");
		log.info("\t > jobKeyB = {}", jobKeyB);
		return ResponseEntity.ok("startJobB SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/stop")
	public ResponseEntity<Object> stopAllJobs() throws SchedulerException {
		log.info("## stopAllJobs");
		JobKey jobKeyA = JobKey.jobKey("jobA");
		JobKey jobKeyB = JobKey.jobKey("jobB");
		log.info("\t > jobKeyA = {}", jobKeyA);
		log.info("\t > jobKeyB = {}", jobKeyB);
		scheduler.pauseJob(jobKeyA);
		scheduler.pauseJob(jobKeyB);
		return ResponseEntity.ok("stopAllJobs SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/stop/job-a")
	public ResponseEntity<Object> stopJobA() throws SchedulerException {
		log.info("## stopJobA");
		JobKey jobKeyA = JobKey.jobKey("jobA");
		log.info("\t > jobKeyA = {}", jobKeyA);
		scheduler.pauseJob(jobKeyA);
		return ResponseEntity.ok("stopJobA SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/stop/job-b")
	public ResponseEntity<Object> stopJobB() throws SchedulerException {
		log.info("## stopJobB");
		JobKey jobKeyB = JobKey.jobKey("jobB");
		log.info("\t > jobKeyB = {}", jobKeyB);
		scheduler.pauseJob(jobKeyB);
		return ResponseEntity.ok("stopJobB SUCCESS");
	}
	
}
