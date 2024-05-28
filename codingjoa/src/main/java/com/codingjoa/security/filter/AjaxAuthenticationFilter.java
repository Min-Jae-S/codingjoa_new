package com.codingjoa.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
public class AjaxAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private static final String USERNAME_KEY = "memberId";
	private static final String PASSWORD_KEY = "memberPassword";
	private ObjectMapper objectMapper = new ObjectMapper();

	public AjaxAuthenticationFilter() {
		super(new AntPathRequestMatcher("/login", "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		return null;
	}
	
	

}
