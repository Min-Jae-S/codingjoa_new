package com.codingjoa.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestSchedulerController {

	/*
	 *	1. Java 내부 클래스를 이용한 스케쥴링 (Timer, ScheduledExecutorService) 
	 * 	2. Open Source를 이용한 스케쥴링 (Quartz)
	 * 		- Job, Trigger, Scheduler
	 */
	
	@GetMapping("/scheduler")
	public String main() {
		log.info("## TestScheduler main");
		return "test/scheduler";
	}
	
	@ResponseBody
	@GetMapping("/scheduler/timer")
	public ResponseEntity<Object> timer() {
		log.info("## timer");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd(E) HH:mm:ss");
		log.info("\t > Current time: {}", LocalDateTime.now().format(dtf));
		log.info("\t > Current thread's name: {}", Thread.currentThread().getName());
		
		TimerTask task = new TimerTask() {
            public void run() {
            	log.info("## Task performed on: {}", LocalDateTime.now().format(dtf));
            	log.info("## Thread's name: {}", Thread.currentThread().getName());
            }
        };
        Timer timer = new Timer();
        long delay = 5000L;
        timer.schedule(task, delay);
        
		return ResponseEntity.ok("timer success");
	}

	@ResponseBody
	@GetMapping("/scheduler/startTimer")
	public ResponseEntity<Object> startTimer() {
		log.info("## startTimer");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd(E) HH:mm:ss");
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
            	log.info("## Repeated task performed on: {}", LocalDateTime.now().format(dtf));
			}
		}, 0, 1000);
		return ResponseEntity.ok("startTimer success");
	}

	@ResponseBody
	@GetMapping("/scheduler/stopTimer")
	public ResponseEntity<Object> stopTimer() {
		log.info("## stopTimer");
		return ResponseEntity.ok("stopTimer success");
	}
	
}
