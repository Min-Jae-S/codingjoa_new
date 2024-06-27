package com.codingjoa.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import com.codingjoa.service.UrlValidationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UrlValidationServiceImpl implements UrlValidationService {

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();
	
	@Override
	public boolean validateUrl(HttpServletRequest request, String url) {
		log.info("## validateUrl");
		String baseUrl = getBaseURL(request);
		return antPathMatcher.match(baseUrl + "/**", url);
	}
	
	private String getBaseURL(HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL(); 	// http://localhost:8888/codingjoa/**
		String contextPath = request.getContextPath();		// /codingjoa
		
		int contextPathIndex = requestURL.indexOf(contextPath) + contextPath.length();
		return requestURL.substring(0, contextPathIndex);
	}
	
}
