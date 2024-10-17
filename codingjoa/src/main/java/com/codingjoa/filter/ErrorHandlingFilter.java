package com.codingjoa.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.codingjoa.util.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorHandlingFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpSevletRequest = (HttpServletRequest) request;
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(httpSevletRequest));
		log.info("\t > x-requested-with = {}", httpSevletRequest.getHeader("x-requested-with"));
		
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			
		}
	}
	
	private boolean isAjaxRequest(HttpServletRequest httpSevletRequest) {
		return "XMLHttpRequest".equals(httpSevletRequest.getHeader("x-requested-with"));
	}
	
}
