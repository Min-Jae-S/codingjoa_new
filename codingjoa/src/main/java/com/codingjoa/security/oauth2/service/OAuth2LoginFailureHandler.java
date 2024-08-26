package com.codingjoa.security.oauth2.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		if (exception instanceof OAuth2AuthenticationException) {
			OAuth2AuthenticationException e = (OAuth2AuthenticationException) exception;
			log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
		}
	}

}
