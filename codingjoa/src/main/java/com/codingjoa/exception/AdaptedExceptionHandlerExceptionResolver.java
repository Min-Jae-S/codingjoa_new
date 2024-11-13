package com.codingjoa.exception;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdaptedExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {
	
	private final ExceptionHandlerExceptionResolver baseResolver;
	
	public AdaptedExceptionHandlerExceptionResolver(ExceptionHandlerExceptionResolver baseResolver) {
		this.baseResolver = baseResolver;
	}
	
	@Override
	public void afterPropertiesSet() {
		log.info("## {}.afterPropertiesSet", this.getClass().getSimpleName());
		this.setApplicationContext(baseResolver.getApplicationContext());
		this.setMessageConverters(baseResolver.getMessageConverters());
		this.setResponseBodyAdvice(Collections.singletonList(new JsonViewResponseBodyAdvice()));
		super.afterPropertiesSet();
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		return super.resolveException(request, response, handler, ex);
	}

	@Override
	protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
			Exception exception) {
		log.info("## {}.getExceptionHandlerMethod", this.getClass().getSimpleName());
		log.info("\t > handlerMethod = {}", handlerMethod);
		
		ServletInvocableHandlerMethod invocableHandlerMethod = super.getExceptionHandlerMethod(handlerMethod, exception);
		log.info("\t > invocableHandlerMethod from super = {}", invocableHandlerMethod);
		log.info("\t > exceptionHandlerAdviceCache super = {}", super.getExceptionHandlerAdviceCache());
		
		return invocableHandlerMethod;
	}
	
}
