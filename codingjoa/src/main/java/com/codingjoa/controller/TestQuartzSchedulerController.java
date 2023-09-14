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
	 * 	> Job, Trigger
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
	public ResponseEntity<Object> config() throws SchedulerException {
		log.info("## config");
		log.info("\t > autoStartup        = {}", schedulerFactoryBean.isAutoStartup());
		log.info("\t > running            = {}", schedulerFactoryBean.isRunning());
		log.info("\t > inStandbyMode      = {}", scheduler.isInStandbyMode());
		log.info("\t > started            = {}", scheduler.isStarted());
		log.info("\t > shutdown           = {}", scheduler.isShutdown());
		log.info("\t > registerd jobs     = {}", scheduler.getJobKeys(GroupMatcher.anyJobGroup()));
		log.info("\t > registerd triggers = {}", scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup()));
		
//		Set<String> pausedTriggerGroups = scheduler.getPausedTriggerGroups();
//		Set<TriggerKey> pausedTriggerKeys = new HashSet<>();
//		for (String triggerGroup : pausedTriggerGroups) {
//			 GroupMatcher<TriggerKey> matcher = GroupMatcher.triggerGroupEquals(triggerGroup);
//			 Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(matcher);
//			 pausedTriggerKeys.addAll(triggerKeys);
//		}
//		log.info("\t > paused triggers    = {}", pausedTriggerKeys);
		
		return ResponseEntity.ok("success");
	}
	
	@ResponseBody
	@GetMapping("/quartz/state")
	public ResponseEntity<Object> state() throws SchedulerException {
		log.info("## state");
		schedulerService.state();
		return ResponseEntity.ok("success");
	}

	@ResponseBody
	@GetMapping("/quartz/resume")
	public ResponseEntity<Object> resume() throws SchedulerException {
		log.info("## resume");
		scheduler.resumeAll();
		
		String msg = "resume all jobs";
		log.info("\t > {}", msg);
		
		return ResponseEntity.ok(msg);
	}

	@ResponseBody
	@GetMapping("/quartz/resume/job-a")
	public ResponseEntity<Object> resumeJobA() throws SchedulerException {
		log.info("## resumeJobA");
		
		String msg = (schedulerService.resumeJobA() == true) ? 
				"resume JobA" : "JobA is already running";
		log.info("\t > {}", msg);
		
		return ResponseEntity.ok(msg);
	}

	@ResponseBody
	@GetMapping("/quartz/resume/job-b")
	public ResponseEntity<Object> resumeJobB() throws SchedulerException {
		log.info("## resumeJobB");
		
		String msg = (schedulerService.resumeJobB() == true) ? 
				"resume JobB" : "JobB is already running";
		log.info("\t > {}", msg);
		
		return ResponseEntity.ok(msg);
	}

	@ResponseBody
	@GetMapping("/quartz/pause")
	public ResponseEntity<Object> pause() throws SchedulerException {
		log.info("## pause");
		scheduler.pauseAll();
		
		String msg = "pause all jobs";
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
