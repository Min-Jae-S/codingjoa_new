package com.codingjoa.scheduling.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SchedulingService {

	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss");

	//@Scheduled(cron = "0/5 * * * * ?") // return void & no parameter
	@Scheduled(fixedRate = 2000)
	public void run() {
		log.info("## repeated task performed on: {} \t [{}]", 
				LocalDateTime.now().format(dtf), Thread.currentThread().getName());
	}
}
