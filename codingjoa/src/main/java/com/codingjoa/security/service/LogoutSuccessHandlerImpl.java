package com.codingjoa.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
	
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		String continueUrl = resolveContinueUrl(request);
		redirectStrategy.sendRedirect(request, response, continueUrl);
	}
	
	private String resolveContinueUrl(HttpServletRequest request) {
		String continueUrl = request.getParameter("continue");
		
		if (!isValidUrl(continueUrl, request)) {
			log.info("\t > missing or invalid continueUrl provided, default continueUrl will be used");
			return ServletUriComponentsBuilder.fromContextPath(request)
					.path("/")
					.build()
					.toUriString();
		} else {
			log.info("\t > valid continueUrl provided, this continueUrl will be used");
			return continueUrl;
		}
	}
	
	private boolean isValidUrl(String url, HttpServletRequest request) {
		if (!StringUtils.hasText(url)) {
			return false;
		}
		
		String pattern = ServletUriComponentsBuilder.fromContextPath(request)
				.path("/**")
				.build()
				.toUriString();
		return new AntPathMatcher().match(pattern, url);
	}
	

}
