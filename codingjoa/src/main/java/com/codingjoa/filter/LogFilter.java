package com.codingjoa.filter;

import java.io.IOException;
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
		log.info("-------- {} init --------", filterConfig.getFilterName());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("-------- {} --------", this.getClass().getSimpleName());
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		String uuid = UUID.randomUUID().toString();
		
		// force to send error(422)
		//HttpServletResponse httpResponse = (HttpServletResponse) response;
		//httpResponse.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());
		
		try {
			log.info("## Request - Before Filter");
			log.info("\t URI = {}", requestURI);
			log.info("\t UUID = {}", uuid);
			log.info("\t distpatcherType = {}", request.getDispatcherType());
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.info("## Catch Exception");
			log.info("\t exception = {}", e.getClass().getSimpleName());
			log.info("\t message = {}", e.getMessage());
			throw e;
		} finally {
			log.info("## Response - After Filter");
			log.info("\t URI = {}", requestURI);
			log.info("\t UUID = {}", uuid);
			log.info("\t distpatcherType = {}", request.getDispatcherType());
		}
	}
	
	@Override
	public void destroy() {
		Filter.super.destroy();
	}
	
}
