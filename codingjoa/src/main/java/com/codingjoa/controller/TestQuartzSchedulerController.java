package com.codingjoa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.scheduler.service.SchedulerService;

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
	 * 	> start (scheduleJob, resumeJob, triggerJob, addJob)
	 * 	> stop (interrupt, unscheduleJob, pauseJob, deleteJob)
	 */
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	private Scheduler scheduler;
	
	@Autowired
	private SchedulerService schedulerService;
	
	@GetMapping("/quartz")
	public String main() {
		log.info("## quartz main");
		return "test/scheduler-quartz";
	}

	@ResponseBody
	@GetMapping("/quartz/config")
	public  ResponseEntity<Object> config() throws SchedulerException {
		log.info("## config");
		log.info("\t > autoStartup = {}", schedulerFactoryBean.isAutoStartup());
		log.info("\t > running = {}", schedulerFactoryBean.isRunning());
		log.info("\t > inStandbyMode = {}", scheduler.isInStandbyMode());
		log.info("\t > started = {}", scheduler.isStarted());
		log.info("\t > shutdown = {}", scheduler.isShutdown());
		
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
		log.info("\t > jobs = {}", jobKeys);
		
		Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
		log.info("\t > triggers = {}", triggerKeys);
		
		log.info("\t ================================================================================");
		log.info("\t > from @Bean                scheduler = {}", scheduler);
		log.info("\t > from schedulerFactoryBean scheudler = {}", schedulerFactoryBean.getObject());
		
		return ResponseEntity.ok("config SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/start")
	public ResponseEntity<Object> start() throws SchedulerException {
		log.info("## start");
		String msg = null;
		if (!scheduler.isStarted()) {
			scheduler.start();
			msg = "start the paused scheduler";
		} else {
			msg = "scheduler is already started";
		}
		log.info("\t > {}", msg);
		return ResponseEntity.ok(msg);
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-a")
	public ResponseEntity<Object> startJobA() throws SchedulerException {
		log.info("## startJobA");
		schedulerService.startJobA();
		return ResponseEntity.ok("startJobA SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/start/job-b")
	public ResponseEntity<Object> startJobB() throws SchedulerException {
		log.info("## startJobB");
		schedulerService.startJobB();
		return ResponseEntity.ok("startJobB SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/pasue")
	public ResponseEntity<Object> pause() throws SchedulerException {
		log.info("## pause");
		String msg = null;
		if (!scheduler.getPausedTriggerGroups().isEmpty()) {
			scheduler.pauseAll();
			msg = "pause the running scheduler";
		} else {
			msg = "scheduler is already paused";
		}
		log.info("\t > {}", msg);
		return ResponseEntity.ok(msg);
	}

	@ResponseBody
	@GetMapping("/quartz/pause/job-a")
	public ResponseEntity<Object> pauseJobA() throws SchedulerException {
		log.info("## pauseJobA");
		return ResponseEntity.ok("pauseJobA SUCCESS");
	}

	@ResponseBody
	@GetMapping("/quartz/pause/job-b")
	public ResponseEntity<Object> pauseJobB() throws SchedulerException {
		log.info("## pauseJobB");
		return ResponseEntity.ok("pauseJobB SUCCESS");
	}
	
	@SuppressWarnings("unused")
	private void loggingJobsAndTriggers(Scheduler scheduler) throws SchedulerException {
		log.info("\t > jobs & triggers");
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
		Map<String, Object> jobsAndTriggers = new HashMap<>();
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			List<String> triggerNames = triggers
					.stream()
					.map(trigger -> trigger.getKey().getName())
					.collect(Collectors.toList());
			jobsAndTriggers.put(jobKey.getName(), triggerNames);
		}
		log.info("\t\t - {}", jobsAndTriggers);
	}
	
}
