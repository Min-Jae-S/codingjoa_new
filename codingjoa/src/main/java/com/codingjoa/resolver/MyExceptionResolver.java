package com.codingjoa.resolver;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, 
			Exception ex) {
		log.info("-------- {} --------", this.getClass().getSimpleName());
		
		log.info("\t > URI = [ {} ] {}", request.getMethod(), getFullURI(request));
		log.info("\t > dispatcherType = {}",  request.getDispatcherType());
		log.info("\t > accept = [ {} ] {}", request.getHeader("accept"));
		log.info("\t > x-requested-with = {}", request.getHeader("x-requested-with"));
		log.info("\t > exception = {}", ex.getClass().getSimpleName());
		
		if (handler == null) {
			log.info("\t > handler is null");
		} else if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			log.info("\t > handler is not null");
			log.info("\t > handler = {} [ {} ]", 
					handlerMethod.getClass().getSimpleName(), handlerMethod.getBeanType().getSimpleName());
		} else {
			//ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handler;
			log.info("\t > handler is not null");
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
	    	return requestURI.append('?')
	    			.append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }
	}
}
