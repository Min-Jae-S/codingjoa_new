package com.codingjoa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@ComponentScan("com.codingjoa.quartz")
@RequiredArgsConstructor
@Configuration
public class QuartzConfig {

//	@Bean
//	public SchedulerFactoryBean schedulerFactoryBean() {
//		SchedulerFactoryBean factory = new SchedulerFactoryBean();
//		return factory;
//	}
//	
//	@Bean
//	public Scheduler scheduler(SchedulerFactoryBean factory) throws SchedulerException {
//		return factory.getObject();
//	}
	
}
