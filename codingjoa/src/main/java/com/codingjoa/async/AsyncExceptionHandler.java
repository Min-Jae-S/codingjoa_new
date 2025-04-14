package com.codingjoa.async;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/*
 * Handler for exceptions thrown during the execution of asynchronous (@Async) methods
 * Since exceptions in async methods run on separate threads and are not propagated to the caller,
 * this handler allows for logging or forwarding those exceptions to external alerting systems (ex. Slack).
 * 
 * SlackService integration is not implemented, but the structure is designed to be easily extendable for future alerting integrations.
 */

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
	
	//private fianl SlackService slackService;
	
	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		log.info("## {}.handleUncaughtEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", ex.getClass().getSimpleName(), ex.getMessage());
		
		//slackService.send(message);
	}

}
