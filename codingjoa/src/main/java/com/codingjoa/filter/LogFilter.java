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
public class LogFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("## {} init...", this.getClass().getSimpleName());
	}

	@SuppressWarnings("unused")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("## {} doFilter...", this.getClass().getSimpleName());
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		String uuid = UUID.randomUUID().toString();
		
		chain.doFilter(request, response);
		
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

	@Override
	public void destroy() {
		log.info("## {} destory...", this.getClass().getSimpleName());
	}
}
