package com.codingjoa.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@DisallowConcurrentExecution // 클러스터링 환경에선 해당 어노테이션 작동하지 않음
public class JobA extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		log.info("## {}, repeated task performed on: {}", this.getClass().getSimpleName(), LocalDateTime.now().format(dtf));
	}
}
