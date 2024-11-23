package com.codingjoa.controller.test;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.codingjoa.dto.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@RequestMapping("/test/quartz2")
@RestController
public class TestQuartz2Controller {
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private SchedulerFactoryBean schedulerFactory;

	@Autowired
	private Scheduler scheduler;

	@GetMapping("/test1")
	public ResponseEntity<Object> test1() throws SchedulerException {
		log.info("## test1");
		log.info("\t > schedulerFactory = {}", schedulerFactory.getClass().getSimpleName());
		log.info("\t   - isRunning = {}", schedulerFactory.isRunning());
		log.info("\t   - isAutoStartup = {}", schedulerFactory.isAutoStartup());
		
		log.info("\t > scheduler = {}", scheduler.getClass().getSimpleName());
		log.info("\t   - isStarted = {}", scheduler.isStarted());
		log.info("\t   - isInStandbyMode = {}", scheduler.isInStandbyMode());
		log.info("\t   - isShutdown = {}", scheduler.isShutdown());
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		log.info("\t > start scheduler");
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test3")
	public ResponseEntity<Object> test3() {
		log.info("## test3");
		log.info("\t > pause scheduler");
		try {
			scheduler.standby();
		} catch (SchedulerException e) {
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		}
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
}
