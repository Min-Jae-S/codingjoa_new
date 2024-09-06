package com.codingjoa.security.oauth2.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.util.MessageUtils;
import com.codingjoa.util.UriUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		String message = MessageUtils.getMessage("error.OAuth2Login");
		
		if (exception instanceof OAuth2AuthenticationException) {
			OAuth2AuthenticationException e = (OAuth2AuthenticationException) exception;
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getError());
		} else {
			log.info("\t > {}: {}", exception.getClass().getSimpleName(), exception.getMessage());
		}
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.message(message)
				.details(Map.of("redirectUrl", UriUtils.buildDefaultLoginUrl(request)))
				.build();
		
		request.setAttribute("errorResponse", errorResponse);
		request.getRequestDispatcher("/WEB-INF/views/feedback/failure.jsp").forward(request, response);
	}

}
