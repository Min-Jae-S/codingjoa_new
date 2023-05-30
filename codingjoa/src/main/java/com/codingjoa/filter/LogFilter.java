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
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j //@WebFilter
public class LogFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String fullURI = getFullURI(httpRequest);
		String method = httpRequest.getMethod();
		String uuid = UUID.randomUUID().toString();
		
		try {
			log.info("## {} : request", this.getClass().getSimpleName());
			log.info("\t > URI = {} '{}'", method, fullURI);
			log.info("\t > UUID = {}", uuid);
			log.info("\t > dispatcherType = {}", httpRequest.getDispatcherType());
			log.info("\t > accept = {}", httpRequest.getHeader("accept")); // The header name is case insensitive.
			log.info("\t > x-requested-with = {}", httpRequest.getHeader("x-requested-with"));
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.info("## catch Exception");
			log.info("\t > exception = {}", e.getClass().getSimpleName());
			log.info("\t > message = {}", e.getMessage());
			throw e;
		}
		
		log.info("## {} : response", this.getClass().getSimpleName());
		log.info("\t > URI = {} '{}'", method, fullURI);
		log.info("\t > UUID = {}", uuid);
		log.info("\t > dispatcherType = {}", httpRequest.getDispatcherType());
		log.info("\t > contentType = {}", httpResponse.getContentType());
		log.info("\t > status = {}", httpResponse.getStatus());
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
