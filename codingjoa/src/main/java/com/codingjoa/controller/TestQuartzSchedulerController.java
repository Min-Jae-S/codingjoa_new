package com.codingjoa.controller;

import org.quartz.Scheduler;
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
	
	@SuppressWarnings("unused")
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@GetMapping("/quartz")
	public String main() {
		log.info("## main");
		return "test/quartz";
	}

	@ResponseBody
	@GetMapping("/quartz/config")
	public  ResponseEntity<Object> config() {
		log.info("## config");
		log.info("\t > scheduler = {}",  schedulerFactoryBean.getScheduler());
		log.info("\t > isAutoStartup = {}", schedulerFactoryBean.isAutoStartup());
		log.info("\t > isRunning = {}", schedulerFactoryBean.isRunning());
		return ResponseEntity.ok("config success");
	}

	@ResponseBody
	@GetMapping("/quartz/start")
	public ResponseEntity<Object> startQuartz() {
		log.info("## startQuartz");
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		return ResponseEntity.ok("startQuartz success");
	}

	@ResponseBody
	@GetMapping("/quartz/stop")
	public ResponseEntity<Object> stopQuartz() {
		log.info("## stopQuartz");
		return ResponseEntity.ok("stopQuartz success");
	}
	
}
