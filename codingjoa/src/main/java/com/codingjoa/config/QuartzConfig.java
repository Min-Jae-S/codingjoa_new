package com.codingjoa.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import lombok.RequiredArgsConstructor;

@ComponentScan("com.codingjoa.quartz")
@RequiredArgsConstructor
@Configuration
public class QuartzConfig {

	
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setJobFactory(jobFactory());
		//schedulerFactory.setAutoStartup(false);
		schedulerFactory.setOverwriteExistingJobs(true);
		schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
		//schedulerFactory.setQuartzProperties(quartzProperties());
		return schedulerFactory;
	}
	
	@Bean
	public Scheduler scheduler() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean().getObject();
		scheduler.pauseAll();
		scheduler.start();
		return scheduler;
	}
	
	@Bean
	public SpringBeanJobFactory jobFactory() {
		return new SpringBeanJobFactory();
	}

	/*
	 * for working annotation @Autowired in job classes
	 * custom job factory of spring with DI support for @Autowired
	 */
	
//	@Bean
//	public SpringBeanJobFactory jobFactory() {
//		log.info("## AutowiringSpringBeanJobFactory");
//		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
//	    jobFactory.setApplicationContext(applicationContext);
//	    return jobFactory;
//	}
	
//	@Bean
//	public Properties quartzProperties() {
//		 PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//		 Properties properties = null;
//		 return properties;
//	}
	

	
}
