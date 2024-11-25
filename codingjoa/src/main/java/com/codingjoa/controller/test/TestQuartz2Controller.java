package com.codingjoa.controller.test;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.quartz.Job1;
import com.codingjoa.quartz.Job2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/quartz2")
@RestController
public class TestQuartz2Controller {
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private SchedulerFactoryBean schedulerFactory;

	@Autowired
	private Scheduler scheduler;
	
	@Autowired(required = false)
	private Job1 job1;
	
	@Autowired(required = false)
	private Job2 job2;

	@GetMapping("/config")
	public ResponseEntity<Object> config() throws SchedulerException {
		log.info("## config");
		log.info("\t > schedulerFactory = {}", schedulerFactory.getClass().getSimpleName());
		log.info("\t   - isRunning = {}", schedulerFactory.isRunning());
		log.info("\t   - isAutoStartup = {}", schedulerFactory.isAutoStartup());
		
		log.info("\t > scheduler = {}", scheduler.getClass().getSimpleName());
		log.info("\t   - isStarted = {}", scheduler.isStarted());
		log.info("\t   - isInStandbyMode = {}", scheduler.isInStandbyMode());
		log.info("\t   - isShutdown = {}", scheduler.isShutdown());
		
		log.info("\t > job1 (Job impl) = {}", job1);
		log.info("\t > job2 (QuartzJobBean impl) = {}", job2);

		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/start")
	public ResponseEntity<Object> start() throws SchedulerException {
		log.info("## start");
		scheduler.start();
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/standby")
	public ResponseEntity<Object> standby() throws SchedulerException {
		log.info("## standby");
		scheduler.standby();
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/shutdown")
	public ResponseEntity<Object> shutdown() throws SchedulerException {
		log.info("## shutdown");
		scheduler.shutdown();
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
}
