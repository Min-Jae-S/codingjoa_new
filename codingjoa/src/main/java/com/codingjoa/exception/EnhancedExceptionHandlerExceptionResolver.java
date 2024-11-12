package com.codingjoa.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EnhancedExceptionHandlerExceptionResolver implements HandlerExceptionResolver {
	
	private final ExceptionHandlerExceptionResolver delegate;
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		log.info("## {}.resolveException", this.getClass().getSimpleName());
		
		Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache = delegate.getExceptionHandlerAdviceCache();
		exceptionHandlerAdviceCache.forEach((key, value) -> log.info("\t > {}, {}", key, value));
		
		return delegate.resolveException(request, response, exceptionHandlerAdviceCache, ex);
	}
	
}
