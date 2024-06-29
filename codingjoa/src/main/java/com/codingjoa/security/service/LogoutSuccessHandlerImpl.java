package com.codingjoa.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
	
	private final AntPathMatcher antPathMatcher = new AntPathMatcher();
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > authentication = {}", authentication);
		
		String redirectUrl = request.getParameter("redirect");
		
		if (!isValidUrl(request, redirectUrl)) {
			log.info("\t > invalid redirectUrl - default redirectUrl will be set");
			redirectUrl = ServletUriComponentsBuilder.fromContextPath(request)
					.path("/")
					.build()
					.toString();
		} else {
			log.info("\t > valid redirectUrl");
		}
		
		log.info("\t > redirect to '{}'", redirectUrl);
		response.sendRedirect(redirectUrl);
		//redirectStrategy.sendRedirect(request, response, redirectUrl);
	}
	
	private boolean isValidUrl(HttpServletRequest request, String url) {
		if (!StringUtils.hasText(url)) {
			return false;
		}
		
		StringBuffer requestURL = request.getRequestURL(); 				// http://localhost:8888/codingjoa/**
		String contextPath = request.getContextPath();					// /codingjoa
		
		int contextPathIndex = requestURL.indexOf(contextPath) + contextPath.length();
		String baserUrl = requestURL.substring(0, contextPathIndex);	// http://localhost:8888/codingjoa
		
		return antPathMatcher.match(baserUrl + "/**", url);
	}

}
