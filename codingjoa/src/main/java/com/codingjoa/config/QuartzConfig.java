package com.codingjoa.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.codingjoa.quartz.AutowiringJobFactory;

import lombok.RequiredArgsConstructor;

@ComponentScan("com.codingjoa.quartz")
@RequiredArgsConstructor
@Configuration
public class QuartzConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) {
		AutowiringJobFactory jobFactory = new AutowiringJobFactory();
		jobFactory.setApplicationContext(applicationContext);

		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setJobFactory(jobFactory);
		
		return schedulerFactory;
	}
	
}
