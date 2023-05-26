package com.codingjoa.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@WebFilter
public class LogFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("## {} init", filterConfig.getFilterName());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("-------- {} --------", this.getClass().getSimpleName());
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String fullURI = getFullURI(httpRequest);
		String method = httpRequest.getMethod();
		String uuid = UUID.randomUUID().toString();
		String dispatcherType = httpRequest.getDispatcherType().toString();

		try {
			log.info("## Request");
			log.info("\t > URI = {} '{}'", method, fullURI);
			log.info("\t > UUID = {}", uuid);
			log.info("\t > dispatcherType = {}", dispatcherType);
			log.info("\t > accept = {}", httpRequest.getHeader("accept"));
			log.info("\t > x-requested-with = {}", httpRequest.getHeader("x-requested-with"));
			log.info("\t > parameter:");
			request.getParameterMap().forEach((key, value) -> log.info("\t\t {} = {}", key, value));
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.info("## Catch Exception");
			log.info("\t > exception = {}", e.getClass().getSimpleName());
			log.info("\t > message = {}", e.getMessage());
			throw e;
		}
		
//		log.info("## Response");
//		log.info("\t > URI = {} '{}'", method, fullURI);
//		log.info("\t > UUID = {}", uuid);
//		log.info("\t > dispatcherType = {}", dispatcherType);
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
