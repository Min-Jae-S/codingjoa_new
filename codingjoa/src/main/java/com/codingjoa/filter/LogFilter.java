package com.codingjoa.filter;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			log.info("## Request");
			log.info("\t URI = {}", requestURI);
			log.info("\t distpatcherType = {}",  request.getDispatcherType());
			log.info("\t contentType = {}", httpResponse.getContentType());
			
//			Iterator<String> headerIterator = httpRequest.getHeaderNames().asIterator();
//			while(headerIterator.hasNext()) {
//				String header = headerIterator.next();
//				log.info("\t {} = {}", header, httpRequest.getHeader(header));
//			}
			
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.info("exception = {}", e.getClass().getSimpleName());
			log.info("message = {}", e.getMessage());
			throw e;
		} finally {
			log.info("## Response");
			log.info("\t URI = {}", requestURI);
			log.info("\t distpatcherType = {}",  request.getDispatcherType());
			log.info("\t contentType = {}", httpResponse.getContentType());
			
//			httpResponse.getHeaderNames().forEach(header -> {
//				log.info("\t {} = {}", header, httpResponse.getHeader(header));
//			});
		}
	}
	
	@Override
	public void destroy() {
		Filter.super.destroy();
	}
	
}
