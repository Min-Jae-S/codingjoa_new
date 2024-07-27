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
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		String redirectUrl = resolveRedirectUrl(request);
		log.info("\t > redirect to '{}'", redirectUrl);
		
		response.sendRedirect(redirectUrl);
		//redirectStrategy.sendRedirect(request, response, redirectUrl);
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
	
	private String resolveRedirectUrl(HttpServletRequest request) {
		String redirectUrl = request.getParameter("redirect");
		
		if (!isValidUrl(redirectUrl, request)) {
			log.info("\t > missing or invalid redirectUrl, setting default redirectUrl");
			return ServletUriComponentsBuilder.fromContextPath(request)
					.path("/")
					.build()
					.toUriString();
		} else {
			log.info("\t > valid redirectUrl, setting redirectUrl from request");
			return redirectUrl;
		}
	}

}
