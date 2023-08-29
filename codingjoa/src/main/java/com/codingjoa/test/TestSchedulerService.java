package com.codingjoa.test;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestSchedulerService {

	@Scheduled(cron = "*/5 * * * * *") 
	// return void & no paramter
	public void runScheduler() {
		log.info("\t > run scheduler");
	}
}
