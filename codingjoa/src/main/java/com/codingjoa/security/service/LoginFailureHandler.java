package com.codingjoa.security.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.util.AjaxUtils;
import com.codingjoa.util.MessageUtils;
import com.codingjoa.util.UriUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

	private static final String FORWARD_PATH = "/WEB-INF/views/feedback/alert-and-redirect.jsp";
	private final ObjectMapper objectMapper;
	Set<Class<?>> handledExceptions = Set.of(UsernameNotFoundException.class, BadCredentialsException.class);
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		
		/*
		 * AuthenticationServiceException (LoginFilter)
		 *  - invalid request, invalid json format
		 *  
		 * UsernameNotFoundException (UserDetailsServiceImpl)
		 * 	- not found user by email
		 * 
		 * BadCredentialsException (LoginFilter, LoginProvider)
		 * 	- no email or no password
		 * 	- not matched password
		 */
		
		String message = MessageUtils.getMessage("error.login");
		
		if (handledExceptions.contains(e.getClass())) {
			message = e.getMessage();
			//message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".<br>"), "<br>");
		}
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.message(message)
				.build();
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		
		if (AjaxUtils.isAjaxRequest(request)) {
			log.info("\t > respond with errorResponse in JSON format");
			String jsonResponse = objectMapper.writeValueAsString(errorResponse);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(jsonResponse);
			response.getWriter().close();
		} else {
			request.setAttribute("continueUrl", UriUtils.buildLoginUrl(request, ""));
			request.setAttribute("message", message);
			
			log.info("\t > forward to 'alert-and-redirect.jsp'");
			request.getRequestDispatcher(FORWARD_PATH).forward(request, response);
		}
	}
}
