package com.codingjoa.config;

import javax.annotation.PostConstruct;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.codingjoa.quartz.AutowiringSpringBeanJobFactory;
import com.codingjoa.quartz.TestJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class QuartzConfig {

	@Autowired
	private ApplicationContext applicationContext;
	
	@PostConstruct
	public void init() {
		log.info("[ QuartzConfig initialized ]");
	}
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		log.info("## SchedulerFactoryBean");
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setJobFactory(jobFactory());
		schedulerFactory.setJobDetails(jobDetail());
		schedulerFactory.setTriggers(trigger());
		schedulerFactory.setOverwriteExistingJobs(true);
		return schedulerFactory;
	}
	
	// for working annotation @Autowired in job classes
	// custom job factory of spring with DI support for @Autowired
	@Bean
	public SpringBeanJobFactory jobFactory() {
		log.info("## AutowiringSpringBeanJobFactory");
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
	    jobFactory.setApplicationContext(applicationContext);
	    return jobFactory;
	}
	
	@Bean
	public JobDetail jobDetail() {
		log.info("## JobDetail");
		return JobBuilder.newJob(TestJob.class)
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger trigger() {
		log.info("## Trigger");
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(10)
						.repeatForever())
				.build();
	}

}
