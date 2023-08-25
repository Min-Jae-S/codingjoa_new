package com.codingjoa.controller;

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
	@GetMapping("/scheduler/startTimer")
	public ResponseEntity<Object> startTimer() {
		log.info("## startTimer");
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
            public void run() {
            	log.info("================================");
            	log.info("## Task executed after 5 seconds");
            	log.info("================================");
            }
        };
        timer.schedule(task, 5000);
		return ResponseEntity.ok("success");
	}
}
