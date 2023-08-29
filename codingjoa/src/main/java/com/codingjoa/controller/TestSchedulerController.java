package com.codingjoa.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.test.TestSchedulerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestSchedulerController {

	/*
	 *	1. Java 내부 클래스를 이용한 스케쥴링 (Timer, ScheduledExecutorService)
	 *		- java.util.Timer : 단일 쓰레드로 동작하며, Timer의 사용은 메모리 누수 및 스레드 관련 문제를 야기할 수 있다.
	 * 	2. Open Source를 이용한 스케쥴링 (Quartz)
	 * 		- Job, Trigger, Scheduler
	 * 
	 * 	Timer can be sensitive to changes in the system clock; ScheduledThreadPoolExecutor isn't.
	 * 	Timer has only one execution thread; ScheduledThreadPoolExecutor can be configured with any number of threads.
	 * 	Runtime Exceptions thrown inside the TimerTask kill the thread, so the following scheduled tasks won't run further; 
	 * 	with ScheduledThreadExecutor, the current task will be cancelled, but the rest will continue to run.
	 * 
	 */
	
	private final Timer timer = new Timer();
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss");
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	@Autowired
	private TestSchedulerService schedulerService;
	
	@GetMapping("/scheduler")
	public String main() {
		log.info("## TestScheduler main");
		return "test/scheduler";
	}
	
	@ResponseBody
	@GetMapping("/scheduler/timer")
	public ResponseEntity<Object> timer() {
		log.info("## timer");
		log.info("\t > Current time: {}", LocalDateTime.now().format(dtf));
		log.info("\t > Current thread's name: {}", Thread.currentThread().getName());
		
		TimerTask task = new TimerTask() {
            public void run() {
            	log.info("\t > Task performed on: {}", LocalDateTime.now().format(dtf));
            	log.info("\t > Thread's name: {}", Thread.currentThread().getName());
            }
        };
        timer.schedule(task, 5000);
		return ResponseEntity.ok("timer success");
	}

	@ResponseBody
	@GetMapping("/scheduler/startTimer")
	public ResponseEntity<Object> startTimer() {
		log.info("## startTimer");
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
            	log.info("\t > Repeated task performed on: {}", LocalDateTime.now().format(dtf));
			}
		}, 0, 1000);
		return ResponseEntity.ok("startTimer success");
	}

	@ResponseBody
	@GetMapping("/scheduler/stopTimer")
	public ResponseEntity<Object> stopTimer() {
		log.info("## stopTimer");
		timer.cancel();
		return ResponseEntity.ok("stopTimer success");
	}

	@ResponseBody
	@GetMapping("/scheduler/startExecutor")
	public ResponseEntity<Object> startExecutor() {
		log.info("## startExecutor");
		executor.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
            	log.info("\t > Repeated task performed on: {}", LocalDateTime.now().format(dtf));
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);
		return ResponseEntity.ok("startExecutor success");
	}

	@ResponseBody
	@GetMapping("/scheduler/stopExecutor")
	public ResponseEntity<Object> stopExecutor() {
		log.info("## stopExecutor");
		executor.shutdown();
		return ResponseEntity.ok("stopExecutor success");
	}

	@ResponseBody
	@GetMapping("/scheduler/run")
	public ResponseEntity<Object> runScheduler() {
		log.info("## runScheduler");
		schedulerService.runScheduler();
		return ResponseEntity.ok("runScheduler success");
	}
	
}
