package com.codingjoa.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisallowConcurrentExecution
@Component
public class JobA extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		log.info("## {}, repeated task performed on: {}  [{}]", this.getClass().getSimpleName(), 
				LocalDateTime.now().format(dtf), Thread.currentThread().getName());
	}
}
