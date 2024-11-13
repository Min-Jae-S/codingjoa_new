package com.codingjoa.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DelegatingExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

	private final ExceptionHandlerExceptionResolver delegate;
	
	public DelegatingExceptionHandlerExceptionResolver(ExceptionHandlerExceptionResolver delegate) {
		this.delegate = delegate;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		log.info("## {}.resolveException", this.getClass().getSimpleName());
		log.info("\t > delegate to the {} for exception resolution", delegate.getClass().getSimpleName());
		return delegate.resolveException(request, response, handler, ex);
	}

	@Override
	protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
			Exception exception) {
		log.info("## {}.getExceptionHandlerMethod", this.getClass().getSimpleName());
		log.info("\t > handlerMethod = {}", handlerMethod);
		
		ServletInvocableHandlerMethod invocableHandlerMethod = super.getExceptionHandlerMethod(handlerMethod, exception);
		log.info("\t > invocableHandlerMethod = {}", invocableHandlerMethod);
		
		return invocableHandlerMethod;
	}

	
}
