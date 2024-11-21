package com.codingjoa.controller.test;

import org.quartz.Job;
import org.quartz.Scheduler;
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
	
	@Autowired(required = false)
	private SchedulerFactoryBean schedulerFactory;

	@Autowired(required = false)
	private Scheduler scheduler;

	@Autowired(required = false)
	private JobFactory jobFactory;
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		log.info("\t > schedulerFactory = {}", schedulerFactory);
		log.info("\t > scheduler = {}", scheduler);
		log.info("\t > matches injected scheduler and from factory = {}", schedulerFactory.getObject().equals(scheduler));
		log.info("\t > jobFactory = {}", jobFactory);
		log.info("\t > jobs from context = {}", context.getBeansOfType(Job.class).values());
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test3")
	public ResponseEntity<Object> test3() {
		log.info("## test3");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
}
