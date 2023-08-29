package com.codingjoa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

	private final int POOL_SIZE = 10;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		log.info("## configureTasks");
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.initialize();
        taskRegistrar.setScheduler(threadPoolTaskScheduler);
        
        log.info(System.lineSeparator());
        log.info("========================================");
        log.info("\t > current thread = {}", Thread.currentThread().getName());
        log.info("========================================");
        log.info(System.lineSeparator());
        
	}

}
