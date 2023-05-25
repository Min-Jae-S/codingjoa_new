package com.codingjoa.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.CharacterEncodingFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncodingFilter extends CharacterEncodingFilter {

	@Override
	protected void initFilterBean() throws ServletException {
		log.info("## {} init", this.getFilterName());
		super.initFilterBean();
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("-------- {} --------", this.getFilterName());
		super.doFilterInternal(request, response, filterChain);
	}
}
