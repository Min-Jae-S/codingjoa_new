package com.codingjoa.security.oauth2.service;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
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
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		log.info("\t > create JWT and issue it as a cookie");
		String jwt = jwtProvider.createJwt(authentication, request);
		ResponseCookie jwtCookie = ResponseCookie.from("ACCESS_TOKEN", jwt)
				.domain("localhost")
				.path(request.getContextPath())
				.maxAge(Duration.ofHours(1))
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax") // strict -> lax
				.build();
		response.setHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

		String continueUrl = resolveContinueUrl(authentication, request);
		SuccessResponse successResponse = SuccessResponse.builder()
				.status(HttpStatus.OK)
				.messageByCode("success.Login")
				.data(Map.of("continueUrl", continueUrl))
				.build();
		
		// opation1 : after forwading to view(jsp), using successResponse, alert message and redirect to contineUrl on the client-side
		// opation2 : directly redirect to continueUrl on the server-side
		redirectStrategy.sendRedirect(request, response, continueUrl);
	}
	
	private String resolveContinueUrl(Authentication authentication, HttpServletRequest request) {
		String continueUrl = (String) authentication.getDetails();
		
		if (!isValidUrl(continueUrl, request)) {
			log.info("\t > missing or invalid continueUrl, default continueUrl resolved");
			return ServletUriComponentsBuilder.fromContextPath(request)
					.path("/")
					.build()
					.toUriString();
		} else {
			log.info("\t > valid continueUrl, this continueUrl resolved");
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
