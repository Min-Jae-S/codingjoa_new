package com.codingjoa.resolver;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, 
			Exception ex) {
		log.info("-------- {} --------", this.getClass().getSimpleName());
		
		log.info("\t > URI = {}", getFullURI(request));
		log.info("\t > method = {}", request.getMethod());
		log.info("\t > dispatcherType = {}",  request.getDispatcherType());
		log.info("\t > x-requested-with = {}", request.getHeader("X-Requested-With"));
		log.info("\t > exception = {}", ex.getClass().getSimpleName());
		log.info("\t > runtime excpetion = {}", ex instanceof RuntimeException);
		
		if (handler == null) {
			log.info("\t > handler = {}", "No handler");
		} else if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			log.info("\t > handler = {} [{}]", 
					handler.getClass().getSimpleName(), method.getBeanType().getSimpleName());
		} else {
			//ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handler;
			log.info("\t > handler = {}", handler.getClass().getSimpleName());
		}

		return null;
	}
	
	private String getFullURI(HttpServletRequest request) {
		StringBuilder requestURI = new StringBuilder(request.getRequestURI().toString());
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURI.toString();
	    } else {
	    	return requestURI.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }
	}
}
