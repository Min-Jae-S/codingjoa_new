package com.codingjoa.exception;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnhancedExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

	@Override
	protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
			Exception exception) {
		log.info("## {}.getExceptionHandlerMethod", this.getClass().getSimpleName());
		log.info("\t > handlerMethod = {}", handlerMethod);
		return super.getExceptionHandlerMethod(handlerMethod, exception);
	}
	
	@Override
	public void afterPropertiesSet() {
		log.info("## {}.afterPropertiesSet", this.getClass().getSimpleName());
		super.afterPropertiesSet();
	}
	
}
