package com.codingjoa.async;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
	
	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		log.info("## {}.handleUncaughtException", this.getClass().getSimpleName());
		log.info("\t > {}: {}", ex.getClass().getSimpleName(), ex.getMessage());
		log.info("\t > method name: {}", method.getName());
		
		log.info("\t > params: ");
		for (Object param : params) {
			log.info("\t\t   - param value: ", param);
		}
	}

}
