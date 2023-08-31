package com.codingjoa.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ComponentScan("com.codingjoa.scheduler")
@EnableScheduling
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
	
	@PostConstruct
	public void init() {
		log.info("[ SchedulerConfig initialized ]");
	}
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		log.info("## configureTasks");
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("scheduling-thread-");
        threadPoolTaskScheduler.initialize();
        taskRegistrar.setScheduler(threadPoolTaskScheduler);
	}

}
