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

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.util.UriUtils;

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
				.path("/")
				.maxAge(Duration.ofHours(1))
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax") // strict -> lax
				.build();
		//response.setHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
		response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

		String continueUrl = (String) authentication.getDetails();
		SuccessResponse successResponse = SuccessResponse.builder()
				.status(HttpStatus.OK)
				.messageByCode("success.Login")
				.data(Map.of("continueUrl", UriUtils.resolveContinueUrl(continueUrl, request)))
				.build();
		
		// opation1 : after forwading to view(jsp), using successResponse, alert message and redirect to contineUrl on the client-side
		// opation2 : directly redirect to continueUrl on the server-side
		redirectStrategy.sendRedirect(request, response, UriUtils.resolveContinueUrl(continueUrl, request));
	}
}
