package com.codingjoa.security.service;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.codingjoa.response.ErrorResponse;
import com.codingjoa.security.exception.LoginRequireFieldException;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = 
			new WebAuthenticationDetailsSource();
	private final String key = UUID.randomUUID().toString();
	private final String DEFAULT_FAILURE_URL = "/member/login";
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > referer = {}", request.getHeader("referer"));
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("\t > current authentication = {}", authentication);
		
		if (authentication == null) {
			SecurityContextHolder.getContext().setAuthentication(createAuthentication(request));
		}
		
		String message = MessageUtils.getMessage("error.Login");
		if (e instanceof LoginRequireFieldException || 
				e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
			message = e.getMessage();
		}
		log.info("\t > excpetion = {}", e.getClass().getSimpleName());
		log.info("\t > original message = {}", message);
		
		message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".<br>"), "<br>");
		log.info("\t > processed message = {}", message);
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.message(message)
				.build();
		log.info("\t > {}", errorResponse);
		
		request.setAttribute("errorResponse", errorResponse);
		request.getRequestDispatcher(DEFAULT_FAILURE_URL).forward(request, response);
	}
	
	// ref) AnonymousAuthenticationFilter#createAuthentication(HttpServletRequest)
	protected Authentication createAuthentication(HttpServletRequest request) {
		log.info("\t > create authentication token which is AnonymousAuthenticationToken");
		
		// null object pattern 
		AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(key, "anonymousUser",
				AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")); 
		auth.setDetails(authenticationDetailsSource.buildDetails(request));
		return auth;
	}
}
