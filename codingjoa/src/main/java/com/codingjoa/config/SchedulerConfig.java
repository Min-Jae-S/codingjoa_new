package com.codingjoa.config;

import javax.annotation.PostConstruct;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.codingjoa.scheduler.JobA;
import com.codingjoa.scheduler.JobB;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ComponentScan("com.codingjoa.scheduler")
@Configuration
public class SchedulerConfig {
	
	@Autowired
	private JobListener jobListener;

	@Autowired
    private TriggerListener triggerListener;
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ SchedulerConfig init");
		log.info("===============================================================");
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setJobFactory(jobFactory());
		schedulerFactory.setGlobalJobListeners(jobListener);
		schedulerFactory.setGlobalTriggerListeners(triggerListener);
		schedulerFactory.setJobDetails(jobDetailA(), jobDetailB());
		schedulerFactory.setTriggers(triggerA(), triggerB());
		schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
		schedulerFactory.setAutoStartup(false);
		return schedulerFactory;
	}
	
	@Bean
	public SpringBeanJobFactory jobFactory() {
		return new SpringBeanJobFactory();
	}

	// for working annotation @Autowired in job classes
	// custom job factory of spring with DI support for @Autowired
//	@Bean
//	public SpringBeanJobFactory jobFactory() {
//		log.info("## AutowiringSpringBeanJobFactory");
//		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
//	    jobFactory.setApplicationContext(applicationContext);
//	    return jobFactory;
//	}
	
	@Bean
	public JobDetail jobDetailA() {
		return JobBuilder.newJob(JobA.class)
				.withIdentity("jobA")
				.storeDurably()
				.build();
	}
	
	@Bean
	public JobDetail jobDetailB() {
		return JobBuilder.newJob(JobB.class)
				.withIdentity("jobB")
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger triggerA() {
		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInSeconds(10)
				.repeatForever();
		
		return TriggerBuilder.newTrigger()
				.forJob(jobDetailA())
				.withIdentity("triggerA")
				.withSchedule(scheduleBuilder)
				.build();
	}

	@Bean
	public Trigger triggerB() {
		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInSeconds(30)
				.repeatForever();
		
		return TriggerBuilder.newTrigger()
				.forJob(jobDetailB())
				.withIdentity("triggerB")
				.withSchedule(scheduleBuilder)
				.build();
	}

}
