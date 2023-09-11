package com.codingjoa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
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
	 * 	> Scheduler, Job, Trigger
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
		log.info("## main");
		return "test/scheduler-quartz";
	}

	@ResponseBody
	@GetMapping("/quartz/config")
	public  ResponseEntity<Object> config() throws SchedulerException {
		log.info("## config");
		log.info("\t > autoStartup = {}", schedulerFactoryBean.isAutoStartup());
		log.info("\t > running = {}", schedulerFactoryBean.isRunning());
		log.info("\t > inStandbyMode = {}", scheduler.isInStandbyMode());
		log.info("\t > started = {}", scheduler.isStarted());
		log.info("\t > shutdown = {}", scheduler.isShutdown());
		
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
		log.info("\t > jobs = {}", jobKeys);
		
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		log.info("\t > triggers = {}", triggerKeys);
		
		return ResponseEntity.ok("config SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/start")
	public ResponseEntity<Object> start() throws SchedulerException {
		log.info("## start");
		scheduler.start();
		return ResponseEntity.ok("start SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-a")
	public ResponseEntity<Object> startJobA() throws SchedulerException {
		log.info("## startJobA");
		schedulerService.startJobA();
		return ResponseEntity.ok("startJobA SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-b")
	public ResponseEntity<Object> startJobB() throws SchedulerException {
		log.info("## startJobB");
		schedulerService.startJobB();
		return ResponseEntity.ok("startJobB SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/stop")
	public ResponseEntity<Object> stop() throws SchedulerException {
		log.info("## stop");
		scheduler.shutdown();
		return ResponseEntity.ok("stop SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/stop/job-a")
	public ResponseEntity<Object> stopJobA() throws SchedulerException {
		log.info("## stopJobA");
		return ResponseEntity.ok("stopJobA SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/stop/job-b")
	public ResponseEntity<Object> stopJobB() throws SchedulerException {
		log.info("## stopJobB");
		return ResponseEntity.ok("stopJobB SUCCESS");
	}
	
	@SuppressWarnings("unused")
	private void loggingJobsAndTriggers(Scheduler scheduler) throws SchedulerException {
		log.info("\t > jobs & triggers");
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
		Map<String, Object> jobsAndTriggers = new HashMap<>();
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			List<String> triggerNames = triggers
					.stream()
					.map(trigger -> trigger.getKey().getName())
					.collect(Collectors.toList());
			jobsAndTriggers.put(jobKey.getName(), triggerNames);
		}
		log.info("\t\t - {}", jobsAndTriggers);
	}
	
}
