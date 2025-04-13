package com.codingjoa.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig extends AsyncConfigurerSupport {

	/*
	 * Transaction Management
	 * 	- When using transactions within asynchronous methods, special care is required.
	 * 	  Methods annotated with @Async run in a separate thread from the caller, meaning that any transaction started within the async method has its own independent lifecycle.
	 *    As a result, even if an exception occurs and a rollback is triggered within the asynchronous method, 
	 *    it will only affect the transaction in that separate thread. It will not impact the transaction context of the calling method.
	 * 
	 * Executor
	 * 	- In Spring, asynchronous processing is typically enabled using the @EnableAsync annotation along with the @Async annotation.
	 *    By default, this setup uses SimpleAsyncTaskExecutor, which creates a new thread for each asynchronous task.
	 *    While this may work in simple scenarios, it can lead to several issues such as resource exhaustion, performance degradation, and scalability problems (scailing).
	 *    Resource Waste: Since a new thread is created for every async task, handling a high number of concurrent requests can lead to excessive thread creation. This increases CPU and memory usage significantly.
	 *    For these reasons, it's generally recommended to use a bounded thread pool like ThreadPoolTaskExecutor instead of SimpleAsyncTaskExecutor.
	 *    
	 *    There are two common ways to define a custom executor in Spring:
	 *    	1. Define the executor directly as a Bean.
	 *    	2. Extend AsyncConfigurerSupport and override the getAsyncExecutor() method.
	 */
	
	@Override
	public Executor getAsyncExecutor() {
		log.info("## getAsyncExecutor");
		log.info("\t > default = {}", super.getAsyncExecutor());
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10); 	// 기본 스레드 수
		executor.setMaxPoolSize(50);	// 최대 스레드 수
		executor.setQueueCapacity(100); // 대기 큐의 용량
		executor.setThreadNamePrefix("Async-");
		executor.initialize();
		return executor;
	}
	
}
