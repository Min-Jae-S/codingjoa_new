package com.codingjoa.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class EnhancedExceptionHandlerExceptionResolver implements HandlerExceptionResolver {

	private final ExceptionHandlerExceptionResolver delegate;
	
	public EnhancedExceptionHandlerExceptionResolver(ExceptionHandlerExceptionResolver delegate) {
		this.delegate = delegate;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		log.info("\t > {}.resolveException", this.getClass().getSimpleName());
		return null;
	}
	
}
