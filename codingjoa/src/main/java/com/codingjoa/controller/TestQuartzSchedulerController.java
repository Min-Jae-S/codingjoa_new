package com.codingjoa.controller;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
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

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		log.info("\t > scheduler = {}",  scheduler);
		log.info("\t   - isInStandbyMode = {}",  scheduler.isInStandbyMode());
		log.info("\t   - isStarted = {}",  scheduler.isStarted());
		log.info("\t   - isShutdown = {}",  scheduler.isShutdown());
		return ResponseEntity.ok("config success");
	}

	@ResponseBody
	@GetMapping("/quartz/start")
	public ResponseEntity<Object> startAllJobs() throws SchedulerException {
		log.info("## startAllJobs");
		return ResponseEntity.ok("startAllJobs success");
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-a")
	public ResponseEntity<Object> startJobA() throws SchedulerException {
		log.info("## startJobA");
		return ResponseEntity.ok("startJobA success");
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-a")
	public ResponseEntity<Object> startJobB() throws SchedulerException {
		log.info("## startJobB");
		return ResponseEntity.ok("startJobB success");
	}

	@ResponseBody
	@GetMapping("/quartz/stop")
	public ResponseEntity<Object> stopAllJobs() throws SchedulerException {
		log.info("## stopAllJobs");
		return ResponseEntity.ok("stopAllJobs success");
	}

	@ResponseBody
	@GetMapping("/quartz/stop/job-a")
	public ResponseEntity<Object> stopJobA() throws SchedulerException {
		log.info("## stopJobA");
		return ResponseEntity.ok("stopJobA success");
	}

	@ResponseBody
	@GetMapping("/quartz/stop/job-b")
	public ResponseEntity<Object> stopJobB() throws SchedulerException {
		log.info("## stopJobB");
		return ResponseEntity.ok("stopJobB success");
	}
	
}
