package com.codingjoa.quartz;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobA extends QuartzJobBean {
	
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}, repeated task performed on: {}  [{}]", this.getClass().getSimpleName(), 
				LocalDateTime.now().format(dtf), Thread.currentThread().getName());
	}
}
