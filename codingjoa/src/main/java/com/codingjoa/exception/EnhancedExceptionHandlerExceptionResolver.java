package com.codingjoa.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnhancedExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {
	
	private final ExceptionHandlerExceptionResolver baseResolver;
	
	public EnhancedExceptionHandlerExceptionResolver(ExceptionHandlerExceptionResolver baseResolver) {
		this.baseResolver = baseResolver;
	}
	
	@Override
	public void afterPropertiesSet() {
		log.info("## {}.afterPropertiesSet", this.getClass().getSimpleName());
		this.setApplicationContext(baseResolver.getApplicationContext());
		this.setMessageConverters(baseResolver.getMessageConverters());
		super.afterPropertiesSet();
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		log.info("## {}.resolveException", this.getClass().getSimpleName());
		return super.resolveException(request, response, handler, ex);
	}

	@Override
	protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
			Exception exception) {
		log.info("## {}.getExceptionHandlerMethod", this.getClass().getSimpleName());
		log.info("\t > handlerMethod = {}", handlerMethod);
		
		ServletInvocableHandlerMethod invocableHandlerMethod = super.getExceptionHandlerMethod(handlerMethod, exception);
		log.info("\t > invocableHandlerMethod = {}", invocableHandlerMethod);
		log.info("\t > exceptionHandlerAdviceCache = {}", super.getExceptionHandlerAdviceCache());
		
		return invocableHandlerMethod;
	}
	
}
