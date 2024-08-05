package com.codingjoa.security.oauth2.service;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.security.service.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtProvider jwtProvider;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		String reidrectUrl = resolveRedirectUrl(authentication, request);
		SuccessResponse successResponse = SuccessResponse.builder()
				.status(HttpStatus.OK)
				.messageByCode("success.Login")
				.data(Map.of("redirectUrl", reidrectUrl))
				.build();
		
		String jwt = jwtProvider.createJwt(authentication, request);
		ResponseCookie jwtCookie = ResponseCookie.from("access_token", jwt)
				.domain("localhost")
				.path(request.getContextPath())
				.maxAge(Duration.ofHours(1))
				.httpOnly(true)
				.secure(true)
				.sameSite("Strict")
				.build();
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
	
	private String resolveRedirectUrl(Authentication authentication, HttpServletRequest request) {
		String redirectUrl = (String) authentication.getDetails();
		
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
