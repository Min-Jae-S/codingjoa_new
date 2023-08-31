package com.codingjoa.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.codingjoa.mapper.ImageMapper;
import com.codingjoa.quartz.AutowiringSpringBeanJobFactory;
import com.codingjoa.quartz.JobA;
import com.codingjoa.quartz.JobB;
import com.codingjoa.service.QuartzService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class QuartzConfig {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		log.info("## schedulerFactoryBean");
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setJobFactory(jobFactory());
		schedulerFactory.setJobDetails(jobDetailA(), jobDetailB());
		schedulerFactory.setTriggers(triggerA(), triggerB());
		schedulerFactory.setOverwriteExistingJobs(true);
		schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
		return schedulerFactory;
	}
	
	@Bean
	public SpringBeanJobFactory jobFactory() {
		log.info("## SpringBeanJobFactory Bean");
		SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
		log.info("\t > quartzService = {}", applicationContext.getBeansOfType(QuartzService.class));
		log.info("\t > imageMapper = {}", applicationContext.getBeansOfType(ImageMapper.class));
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}
	
	// for working annotation @Autowired in job classes
	// custom job factory of spring with DI support for @Autowired
//	@Bean
//	public SpringBeanJobFactory jobFactory() {
//		log.info("## AutowiringSpringBeanJobFactory Bean");
//		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
//	    jobFactory.setApplicationContext(applicationContext);
//	    return jobFactory;
//	}
	
	@Bean
	public JobDetail jobDetailA() {
		return JobBuilder.newJob(JobA.class)
				.storeDurably()
				.build();
	}

	@Bean
	public JobDetail jobDetailB() {
		return JobBuilder.newJob(JobB.class)
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger triggerA() {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetailA())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(10)
						.repeatForever())
				.build();
	}

	@Bean
	public Trigger triggerB() {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetailB())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(20)
						.repeatForever())
				.build();
	}

}
