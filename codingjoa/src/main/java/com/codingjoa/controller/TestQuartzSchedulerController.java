package com.codingjoa.controller;

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
		return "test/quartz";
	}

	@ResponseBody
	@GetMapping("/quartz/config")
	public  ResponseEntity<Object> config() {
		log.info("## config");
		return ResponseEntity.ok("config success");
	}

	@ResponseBody
	@GetMapping("/quartz/start")
	public ResponseEntity<Object> startQuartz() {
		log.info("## startQuartz");
		return ResponseEntity.ok("startQuartz success");
	}

	@ResponseBody
	@GetMapping("/quartz/stop")
	public ResponseEntity<Object> stopQuartz() {
		log.info("## stopQuartz");
		return ResponseEntity.ok("stopQuartz success");
	}
	
}
