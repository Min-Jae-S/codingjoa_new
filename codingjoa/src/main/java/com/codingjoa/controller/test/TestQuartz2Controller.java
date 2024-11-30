package com.codingjoa.controller.test;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.service.SchedulerService;
import com.codingjoa.test.TestSchedulerData;

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
	
	@Qualifier("jobDetailA")
	@Autowired
	private JobDetail jobDetailA;

	@Qualifier("jobDetailB")
	@Autowired
	private JobDetail jobDetailB;
	
	@Qualifier("triggerA")
	@Autowired
	private Trigger triggerA;
	
	@Qualifier("triggerB")
	@Autowired
	private Trigger triggerB;
	
	@Autowired
	private SchedulerService schedulerService;
	
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

	@GetMapping("/current-jobs")
	public ResponseEntity<Object> currentJobs() throws SchedulerException {
		log.info("## currentJobs");
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
		if (jobKeys.isEmpty()) {
			log.info("\t > no scheduled jobs");
		}
		
		for (JobKey jobKey : jobKeys) {
			log.info("\t > {}", jobKey);
			for (Trigger trigger : scheduler.getTriggersOfJob(jobKey)) {
				TriggerKey triggerKey = trigger.getKey();
				log.info("\t    - trigger = {}", triggerKey);
				log.info("\t    - state = {}", scheduler.getTriggerState(triggerKey));
				log.info("\t    - previous = {}", trigger.getPreviousFireTime());
				log.info("\t    - next = {}", trigger.getNextFireTime());
			}
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/clear")
	public ResponseEntity<Object> clear() throws SchedulerException {
		log.info("## clear");
		log.info("\t > before clearing, scheduled jobs = {}", scheduler.getJobKeys(GroupMatcher.anyJobGroup()));
		scheduler.clear();
		log.info("\t > after clearing, scheduled jobs = {}", scheduler.getJobKeys(GroupMatcher.anyJobGroup()));
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/start")
	public ResponseEntity<Object> start() throws SchedulerException {
		log.info("## start");
		if (!scheduler.isStarted()) {
			scheduler.start();
		} else {
			log.info("\t > scheduler is already started");
		}
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/sample-data")
	public ResponseEntity<Object> getSampleData() {
		log.info("## getSampleData");
		List<TestSchedulerData> samapleData = schedulerService.getSampleData();
		samapleData.forEach(testSchedulerData -> {
			log.info("\t > id = {}, jobName = {}, timestamp = {}", 
					testSchedulerData.getId(), testSchedulerData.getJobName(), testSchedulerData.getTimestamp());
		});
		
		SuccessResponse response = SuccessResponse.builder()
				.data(samapleData)
				.message("success")
				.build();
		
		return ResponseEntity.ok(SuccessResponse.builder().data(response).build());
	}

	@DeleteMapping("/sample-data")
	public ResponseEntity<Object> deleteSampleData() {
		log.info("## deleteSampleData");
		List<TestSchedulerData> samapleData = schedulerService.getSampleData();
		samapleData.forEach(testSchedulerData -> {
			log.info("\t > id = {}, jobName = {}, timestamp = {}", 
					testSchedulerData.getId(), testSchedulerData.getJobName(), testSchedulerData.getTimestamp());
		});
		
		SuccessResponse response = SuccessResponse.builder()
				.data(samapleData)
				.message("success")
				.build();
		
		return ResponseEntity.ok(SuccessResponse.builder().data(response).build());
	}
	
	@GetMapping("/schedule/{jobType}")
	public ResponseEntity<Object> scheduleJob(@PathVariable String jobType) throws SchedulerException {
		log.info("## scheduleJob");
		
		if ("a".equals(jobType)) {
			scheduler.scheduleJob(jobDetailA, triggerA);
		} else if ("b".equals(jobType)) {
			scheduler.scheduleJob(jobDetailB, triggerB);
		} else {
			log.info("\t > invalid job type: {}", jobType);
			throw new SchedulerException();
		}
		
		if (!scheduler.isStarted()) {
			scheduler.start();
		}
		
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
