package com.codingjoa.filter.test;

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
public class TestAopFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.info("## {}.doFilter", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine((HttpServletRequest) request));
		
		try {
			//throw new RuntimeException();
			throw new IOException();
		} catch (Exception e) {
			log.info("\t > triggered exception = {}: {}", e.getClass().getSimpleName(), e.getMessage());
			throw e;
		}
		
		//chain.doFilter(request, response);
	}

}
