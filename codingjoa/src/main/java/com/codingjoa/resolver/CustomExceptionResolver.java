package com.codingjoa.resolver;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		log.info("-------- CustomExceptionResolver --------");
		
		HandlerMethod method = (HandlerMethod) handler;
		log.info("uri = {}", getFullURI(request));
		log.info("x-requested-with = {}", request.getHeader("x-requested-with"));
		log.info("exception = {}", ex.getClass().getSimpleName());
		log.info("assigned handler = {}", (method != null) ? method.getBeanType().getSimpleName() : method);

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
