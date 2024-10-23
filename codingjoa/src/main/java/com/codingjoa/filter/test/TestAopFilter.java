package com.codingjoa.filter.test;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestAopFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.info("## {}.doFilter", this.getClass().getSimpleName());
		throw new RuntimeException("occured exception in test filter");
		//chain.doFilter(request, response);
	}

}