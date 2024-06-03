package com.codingjoa.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

	// private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	// Request URL: http://localhost:8888/codingjoa/logout
	// Request Method: GET
	// Status Code: 302 Found
	
	// redirectMethod (GET, POST)
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > authentication token = {}", (authentication != null) ? authentication.getClass().getSimpleName() : authentication);

		String redirectUrl = request.getContextPath() + "/login";
		log.info("\t > redirectUrl = {}", redirectUrl);
		
		//response.sendRedirect(redirectUrl);
	}

}
