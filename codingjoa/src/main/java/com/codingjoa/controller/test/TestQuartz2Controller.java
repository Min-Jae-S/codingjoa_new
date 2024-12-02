package com.codingjoa.controller.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.quartz.AlarmDto;
import com.codingjoa.quartz.AlarmJob;
import com.codingjoa.quartz.JobC;
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
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (JobKey jobKey : jobKeys) {
			log.info("\t > {}", jobKey);
			for (Trigger trigger : scheduler.getTriggersOfJob(jobKey)) {
				TriggerKey triggerKey = trigger.getKey();
				log.info("\t    - trigger = {}", triggerKey);
				log.info("\t    - state = {}", scheduler.getTriggerState(triggerKey));
				
				Date previousFireTime = trigger.getPreviousFireTime();
				Date nextFireTime = trigger.getNextFireTime();
				log.info("\t    - previous = {}", sdf.format(previousFireTime));
				log.info("\t    - next = {}", sdf.format(nextFireTime));
			}
		}
		
		SuccessResponse response = SuccessResponse.builder()
				.data(jobKeys)
				.message("success")
				.build();
		
		return ResponseEntity.ok(response);
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

	@GetMapping("/samples")
	public ResponseEntity<Object> getSamples() {
		log.info("## getSamples");
		List<TestSchedulerData> samples = schedulerService.getSamples();
		
		SuccessResponse response = SuccessResponse.builder()
				.data(samples)
				.message("success")
				.build();
		
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/samples")
	public ResponseEntity<Object> deleteSamples() {
		log.info("## deleteSamples");
		schedulerService.deleteSamples();
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
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
		if (!scheduler.isInStandbyMode()) {
			scheduler.standby();
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/shutdown")
	public ResponseEntity<Object> shutdown() throws SchedulerException {
		log.info("## shutdown");
		if (!scheduler.isShutdown()) {
			scheduler.shutdown();
		}
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test1")
	public ResponseEntity<Object> test1(@RequestParam String id) throws SchedulerException {
		log.info("## test1");
		log.info("\t > id = {}", id);
		
		JobDetail job = JobBuilder.newJob(JobC.class)
				.withIdentity("jobC", "myJobs")
				.usingJobData("id", id)
				.storeDurably()
				.build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
				.forJob(job)
				.withIdentity("triggerC", "myTriggers")
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
				.build();
		
		scheduler.scheduleJob(job, trigger);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@PostMapping("/alarm")
	public ResponseEntity<Object> scheduleAlarm(@RequestBody AlarmDto alarmDto) throws SchedulerException {
		log.info("## scheduleAlarm");
		log.info("\t > alramDto = {}", alarmDto);
		
		JobDetail alarmJob = JobBuilder.newJob(AlarmJob.class)
				.withIdentity("alarmJob", "myJobs")
				.usingJobData("message", alarmDto.getMessage())
				.storeDurably()
				.build();
		
		Trigger alarmTrigger = TriggerBuilder.newTrigger()
				.forJob(alarmJob)
				.withIdentity("alarmTrigger", "myTriggers")
				.build();
		
		scheduler.scheduleJob(alarmJob, alarmTrigger);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

}
