package com.codingjoa.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestSchedulerService {
	
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss");

	//@Scheduled(cron = "0/5 * * * * ?") // return void & no parameter
	@Scheduled(fixedDelay = 1000)
	public void runScheduler() {
		log.info("\t > repeated task performed on: {}", LocalDateTime.now().format(dtf));
	}
}
