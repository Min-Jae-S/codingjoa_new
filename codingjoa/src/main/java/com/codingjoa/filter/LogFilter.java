package com.codingjoa.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
	
	private List<String> excludeUrls;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("## {} init", filterConfig.getFilterName());

		String excludeUrls = filterConfig.getInitParameter("excludeUrls");
		log.info("\t > initParamter excludeUrls = {}", excludeUrls);
		
		if (excludeUrls != null) {
			String contextPath = filterConfig.getServletContext().getContextPath();
			this.excludeUrls = Stream.of(excludeUrls.split(","))
					.map(pattern -> contextPath + pattern.trim())
					.collect(Collectors.toList());
		}
		this.excludeUrls.forEach(excludeUrl -> log.info("\t > excludeUrl = {}", excludeUrl));
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
			log.info("## {} : Request", this.getClass().getSimpleName());
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
		
		log.info("## {} : Response", this.getClass().getSimpleName());
		log.info("\t > URI = {} '{}'", method, fullURI);
		log.info("\t > UUID = {}", uuid);
		log.info("\t > dispatcherType = {}", httpRequest.getDispatcherType());
		log.info("\t > contentType = {}", httpResponse.getContentType());
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
