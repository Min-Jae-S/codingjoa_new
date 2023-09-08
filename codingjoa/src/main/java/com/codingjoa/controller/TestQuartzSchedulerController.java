package com.codingjoa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
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
	
	@Resource(name = "triggerA")
	private Trigger triggerA;
	
	@Resource(name = "triggerA")
	private Trigger triggerB;
	
	private void loggingJobAndTrigger(Scheduler scheduler) throws SchedulerException {
		Map<String, Object> jobsAndTriggers = new HashMap<>();
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
		for (JobKey jobKey : jobKeys) {
			String jobName = jobKey.getName();
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			List<String> triggerNames = triggers
					.stream()
					.map(trigger -> trigger.getKey().getName())
					.collect(Collectors.toList());
			jobsAndTriggers.put(jobName, triggerNames);
		}
		log.info("\t > jobs & triggers");
		log.info("\t\t - {}", jobsAndTriggers);
		log.info("\t > jobs = {}", jobsAndTriggers.keySet());
		log.info("\t > triggers = {}", jobsAndTriggers.values());
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
		loggingJobAndTrigger(scheduler);
		
		return ResponseEntity.ok("config SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/start")
	public ResponseEntity<Object> startAllJobs() throws SchedulerException {
		log.info("## startAllJobs");
		scheduler.scheduleJob(triggerA);
		scheduler.scheduleJob(triggerB);
		scheduler.start();
		return ResponseEntity.ok("startAllJobs SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-a")
	public ResponseEntity<Object> startJobA() throws SchedulerException {
		log.info("## startJobA");
		scheduler.scheduleJob(triggerA);
		scheduler.start();
		return ResponseEntity.ok("startJobA SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-b")
	public ResponseEntity<Object> startJobB() throws SchedulerException {
		log.info("## startJobB");
		scheduler.scheduleJob(triggerB);
		scheduler.start();
		return ResponseEntity.ok("startJobB SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/stop")
	public ResponseEntity<Object> stopAllJobs() throws SchedulerException {
		log.info("## stopAllJobs");
		scheduler.shutdown();
		return ResponseEntity.ok("stopAllJobs SUCCESS");
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
	
}
