package com.codingjoa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@PropertySource("/WEB-INF/properties/quartz.properties")
public class QuartzConfig {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		return schedulerFactory;
	}
	
	// for working annotation @Autowired in job classes
//	@Bean
//	public SpringBeanJobFactory springBeanJobFactory() {
//		AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
//	    jobFactory.setApplicationContext(applicationContext);
//	    return jobFactory;
//	}
	
}
