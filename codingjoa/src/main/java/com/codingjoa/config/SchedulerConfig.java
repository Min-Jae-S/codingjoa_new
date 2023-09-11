package com.codingjoa.config;

import javax.annotation.PostConstruct;

import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
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
		//schedulerFactory.setGlobalJobListeners(jobListener);
		//schedulerFactory.setGlobalTriggerListeners(triggerListener);
		schedulerFactory.setOverwriteExistingJobs(true);
		schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
		return schedulerFactory;
	}
	
	@Bean
	public Scheduler scheduler() {
		return schedulerFactoryBean().getObject();
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

}
