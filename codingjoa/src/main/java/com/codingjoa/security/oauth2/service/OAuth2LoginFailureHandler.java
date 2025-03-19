package com.codingjoa.security.oauth2.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.util.MessageUtils;
import com.codingjoa.util.UriUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

	private static final String FORWARD_PATH = "/WEB-INF/views/feedback/alert-and-redirect.jsp";
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		String message = MessageUtils.getMessage("error.login.oauth2");
		
		if (exception instanceof OAuth2AuthenticationException) {
			OAuth2AuthenticationException e = (OAuth2AuthenticationException) exception;
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getError());
		} else {
			log.info("\t > {}: {}", exception.getClass().getSimpleName(), exception.getMessage());
		}
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		
		request.setAttribute("continueUrl", UriUtils.buildLoginUrl(request, ""));
		request.setAttribute("message", message);
		
		log.info("\t > forward to 'alert-and-redirect.jsp'");
		request.getRequestDispatcher(FORWARD_PATH).forward(request, response);
	}
}
