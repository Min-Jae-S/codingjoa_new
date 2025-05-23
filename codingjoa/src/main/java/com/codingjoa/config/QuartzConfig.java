package com.codingjoa.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class QuartzConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactory(ApplicationContext context) {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

		//AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
		jobFactory.setApplicationContext(context);
		
		schedulerFactory.setJobFactory(jobFactory);
		schedulerFactory.setAutoStartup(true);
		
		// only applies under specific conditions and may not always work when creating Jobs dynamically.
		schedulerFactory.setOverwriteExistingJobs(true);
		
		//schedulerFactory.setJobDetails(jobDetail);
		//schedulerFactory.setTriggers(trigger);
		
		return schedulerFactory;
	}
	
}
