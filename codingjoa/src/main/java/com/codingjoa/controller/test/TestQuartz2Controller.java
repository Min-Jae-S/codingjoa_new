package com.codingjoa.controller.test;

import javax.annotation.Resource;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.codingjoa.dto.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
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
	
	@Resource(name = "triggerA")
	private Trigger triggerA;
	
	@Resource(name = "triggerB")
	private Trigger triggerB;
	
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
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/current")
	public ResponseEntity<Object> current() throws SchedulerException {
		log.info("## current");
		log.info("\t > currently executing jobs = {}", scheduler.getCurrentlyExecutingJobs());
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/start/{jobType}")
	public ResponseEntity<Object> start(@PathVariable String jobType) throws SchedulerException {
		log.info("## start");
		if ("a".equals(jobType)) {
			scheduler.scheduleJob(triggerA);
		} else if ("b".equals(jobType)) {
			scheduler.scheduleJob(triggerB);
		} else {
			log.info("\t > invalid job type: {}", jobType);
			throw new SchedulerException();
		}
		
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
