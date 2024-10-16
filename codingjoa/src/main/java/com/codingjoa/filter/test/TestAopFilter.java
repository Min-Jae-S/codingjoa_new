package com.codingjoa.filter.test;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.codingjoa.util.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestAopFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("## {}.init", filterConfig.getFilterName());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.info("## {}.doFilter", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine((HttpServletRequest) request));
		chain.doFilter(request, response);
		throw new RuntimeException();
	}

}
