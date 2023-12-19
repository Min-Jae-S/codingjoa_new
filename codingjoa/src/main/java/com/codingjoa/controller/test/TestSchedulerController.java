package com.codingjoa.controller.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
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
	
	private Timer timer;
	private ScheduledExecutorService executor;
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	@GetMapping("/scheduler/timer")
	public ResponseEntity<Object> timer() {
		log.info("## timer");
		log.info("\t > current          : {}  [{}]", LocalDateTime.now().format(dtf), Thread.currentThread().getName());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
			@Override
            public void run() {
            	log.info("\t > task performed on: {}  [{}]", LocalDateTime.now().format(dtf), Thread.currentThread().getName());
            	timer.cancel();
            }
        }, 5000);
		return ResponseEntity.ok("timer success");
	}

	@GetMapping("/scheduler/startTimer")
	public ResponseEntity<Object> startTimer() {
		log.info("## startTimer");
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
            	log.info("\t > repeated task performed on: {}  [{}]", 
            			LocalDateTime.now().format(dtf), Thread.currentThread().getName());
			}
		}, 0, 1000);
		return ResponseEntity.ok("startTimer success");
	}

	@GetMapping("/scheduler/stopTimer")
	public ResponseEntity<Object> stopTimer() {
		log.info("## stopTimer");
		timer.cancel();
		return ResponseEntity.ok("stopTimer success");
	}

	@GetMapping("/scheduler/startExecutor")
	public ResponseEntity<Object> startExecutor() {
		log.info("## startExecutor");
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
            	log.info("\t > repeated task performed on: {}  [{}]", 
            			LocalDateTime.now().format(dtf), Thread.currentThread().getName());
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);
		return ResponseEntity.ok("startExecutor success");
	}

	@GetMapping("/scheduler/stopExecutor")
	public ResponseEntity<Object> stopExecutor() {
		log.info("## stopExecutor");
		executor.shutdown();
		return ResponseEntity.ok("stopExecutor success");
	}

}
