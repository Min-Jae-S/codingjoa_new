package com.codingjoa.exception;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExceptionHandlerExceptionResolverImpl extends ExceptionHandlerExceptionResolver {

	@Override
	protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
			Exception exception) {
		log.info("## {}.getExceptionHandlerMethod");
		log.info("\t > handlerMethod = {}", handlerMethod);
		
		if (handlerMethod != null) {
			Method method = handlerMethod.getMethod();
			log.info("\t > method name = {}", method.getName());
		}
		
		ServletInvocableHandlerMethod invocableHandlerMethod = super.getExceptionHandlerMethod(handlerMethod, exception);
		log.info("\t > resolved from handerMethod = {}", invocableHandlerMethod.getResolvedFromHandlerMethod());
		
		return invocableHandlerMethod;
	}

	
}
