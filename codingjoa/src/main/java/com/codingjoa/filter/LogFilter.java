package com.codingjoa.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@WebFilter
public class LogFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("-------- {} --------", this.getClass().getSimpleName());
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		String uuid = UUID.randomUUID().toString();
		
		log.info("BEFORE doFilter");
		chain.doFilter(request, response);
		log.info("AFTER doFilter");
		
//		try {
//			log.info("Request : {}, {}, {}", uuid, request.getDispatcherType(), requestURI);
//			chain.doFilter(request, response);
//		} catch (Exception e) {
//			log.info("exception = {}", e.getClass().getSimpleName());
//			log.info("message = {}", e.getMessage());
//			throw e;
//		} finally {
//			log.info("Response: {}, {}, {}", uuid, request.getDispatcherType(), requestURI);
//		}
		
	}
}
