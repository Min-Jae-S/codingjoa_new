package com.codingjoa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import lombok.RequiredArgsConstructor;

@ComponentScan("com.codingjoa.quartz")
@RequiredArgsConstructor
@Configuration
public class QuartzConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactory() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		return schedulerFactory;
	}
	
}
