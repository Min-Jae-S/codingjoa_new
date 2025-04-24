package com.codingjoa.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codingjoa.util.RequestUtils;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j //@WebFilter
public class LogFilter implements Filter {
	
	private List<String> excludePatterns = new ArrayList<>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("## {}.init", filterConfig.getFilterName());
		String excludePatterns = filterConfig.getInitParameter("excludePatterns");
		log.info("\t > initParamter excludePatterns = '{}'", excludePatterns);
		
		if (excludePatterns != null) {
			String contextPath = filterConfig.getServletContext().getContextPath();
			this.excludePatterns = Stream.of(excludePatterns.split(","))
					.map(excludePattern -> contextPath + excludePattern.trim())
					.collect(Collectors.toList());
		}
		log.info("\t > excludePatterns = {}", this.excludePatterns);
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		String requestURI = request.getRequestURI();
		UUID uuid = UUID.randomUUID();
		
		if (isExcludePattern(requestURI)) {
			chain.doFilter(servletRequest, servletResponse);
		} else {
			log.info("## {}, request-line = {} [{}]", this.getClass().getSimpleName(), RequestUtils.getRequestLine(request), request.getDispatcherType());
			try {
				//logRequestDetails(request, response, uuid);
				chain.doFilter(servletRequest, servletResponse);
			} catch (Exception e) {
				log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
				//throw e;
			} finally {
				//logResponseDetails(request, response, uuid);
			}
		}
	}
	
	private boolean isExcludePattern(String requestURI) {
		for (String excludePattern : excludePatterns) {
			if (requestURI.startsWith(excludePattern)) {
				return true;
			}
		}
		return false;
	}
	
	private void logRequestDetails(HttpServletRequest request, HttpServletResponse response, UUID uuid) {
		log.info("## {} [ REQUEST ]", this.getClass().getSimpleName());
		log.info("\t > UUID = {}", uuid);
		log.info("\t > dispatcherType = {}", request.getDispatcherType());
		log.info("\t > accept = {}", request.getHeader("accept")); // The header name is case insensitive.
		log.info("\t > contentType = {}", response.getContentType());
	}

	private void logResponseDetails(HttpServletRequest request, HttpServletResponse response, UUID uuid) {
		log.info("## {} [ RESPONSE ]", this.getClass().getSimpleName());
		log.info("\t > UUID = {}", uuid);
		log.info("\t > dispatcherType = {}", request.getDispatcherType());
		log.info("\t > contentType = {}", response.getContentType());
	}
	
}
