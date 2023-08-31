package com.codingjoa.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.codingjoa.service.QuartzService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestJob extends QuartzJobBean {
	
	@Autowired
	private QuartzService quartzService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}", this.getClass().getSimpleName());
		quartzService.test();
	}

}
