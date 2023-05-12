package com.codingjoa.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
//@WebFilter
public class LogFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("-------- {} init --------", this.getClass().getSimpleName());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("-------- {} --------", this.getClass().getSimpleName());
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		
		try {
			log.info("## Request");
			log.info("	URI = {}", requestURI);
			log.info("	distpatcherType = {}",  request.getDispatcherType());
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.info("## Catch Exception");
			log.info("	exception = {}", e.getClass().getSimpleName());
			log.info("	message = {}", e.getMessage());
			throw e;
		} finally {
			log.info("## Response");
			log.info("	URI = {}", requestURI);
			log.info("	distpatcherType = {}",  request.getDispatcherType());
		}
	}
	
	@Override
	public void destroy() {
		Filter.super.destroy();
	}
	
}
