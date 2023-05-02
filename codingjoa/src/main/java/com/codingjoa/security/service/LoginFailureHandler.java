package com.codingjoa.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.response.ErrorResponse;
import com.codingjoa.security.exception.LoginRequireFieldException;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

	private final String DEFAULT_FAILURE_URL = "/member/login";
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		log.info("======== LoginFailureHandler ========");
		
		String errorMessage = MessageUtils.getMessage("error.Login");
		
		if(e instanceof LoginRequireFieldException || 
				e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
			errorMessage = e.getMessage();
		}
		log.info("exception = {}, message = {}", e.toString(), errorMessage);
		
		request.setAttribute("errorResponse", ErrorResponse.create().errorMessage(errorMessage));
		request.getRequestDispatcher(DEFAULT_FAILURE_URL).forward(request, response);
	}

}
