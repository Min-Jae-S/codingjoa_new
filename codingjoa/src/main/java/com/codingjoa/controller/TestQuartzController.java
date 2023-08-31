package com.codingjoa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestQuartzController {
	
	/*	
	 * ## Quartz
	 * 	> enable in-memory job scheduler
	 * 	> clustering using database
	 * 	> Scheduler, Job, Trigger
	 * 	> JobDetails, JobDataMap, JobListener, TriggerListener
	 */
	
	@GetMapping("/quartz")
	public String main() {
		log.info("## quartz main");
		return "test/quartz";
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
