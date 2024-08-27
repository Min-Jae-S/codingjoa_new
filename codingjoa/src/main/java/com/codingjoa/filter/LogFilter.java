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

import lombok.extern.slf4j.Slf4j;

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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpSevletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String requestURI = httpSevletRequest.getRequestURI();
		UUID uuid = UUID.randomUUID();
		
		if (isExcludePattern(requestURI)) {
			log.info("## '{}' is excludePattern", requestURI);
			chain.doFilter(request, response);
		} else {
			log.info("## '{}' is includePattern.", requestURI);
			try {
				logRequestDetails(httpSevletRequest, httpServletResponse, uuid);
				chain.doFilter(request, response);
			} catch (Exception e) {
				log.info("## catch Exception");
				log.info("\t > exception = {}", e.getClass().getSimpleName());
				//log.info("\t > message = {}", e.getMessage());
				//throw e;
			} finally {
				logResponseDetails(httpSevletRequest, httpServletResponse, uuid);
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
		log.info("\t > URI = {} '{}'", request.getMethod(), getFullURI(request));
		log.info("\t > UUID = {}", uuid);
		log.info("\t > dispatcherType = {}", request.getDispatcherType());
		log.info("\t > accept = {}", request.getHeader("accept")); // The header name is case insensitive.
		log.info("\t > x-requested-with = {}", request.getHeader("x-requested-with"));
		log.info("\t > contentType = {}", response.getContentType());
	}

	private void logResponseDetails(HttpServletRequest request, HttpServletResponse response, UUID uuid) {
		log.info("## {} [ RESPONSE ]", this.getClass().getSimpleName());
		log.info("\t > URI = {} '{}'", request.getMethod(), getFullURI(request));
		log.info("\t > UUID = {}", uuid);
		log.info("\t > dispatcherType = {}", request.getDispatcherType());
		log.info("\t > contentType = {}", response.getContentType());
	}
	
	private String getFullURI(HttpServletRequest request) {
		StringBuilder requestURI = 
				new StringBuilder(URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8));
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURI.toString();
	    } else {
	    	return requestURI.append('?')
	    			.append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }
	}
}
