package com.codingjoa.security.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.dto.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	private final JwtProvider jwtProvider;
	private final ObjectMapper objectMapper;
	
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
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		
		String continueUrl = resolveContinueUrl(authentication, request);
		SuccessResponse successResponse = SuccessResponse.builder()
				.status(HttpStatus.OK)
				.messageByCode("success.Login")
				.data(Map.of("continueUrl", continueUrl))
				.build();
        
		log.info("\t > respond with success response in JSON format");
		String jsonResponse = objectMapper.writeValueAsString(successResponse);
		response.getWriter().write(jsonResponse);
		response.getWriter().close();
	}
	
	private String resolveContinueUrl(Authentication authentication, HttpServletRequest request) {
		String continueUrl = (String) authentication.getDetails();
		
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
		
//		StringBuffer requestURL = request.getRequestURL(); 				// http://localhost:8888/codingjoa/**
//		String contextPath = request.getContextPath();					// /codingjoa
//		
//		int contextPathIndex = requestURL.indexOf(contextPath) + contextPath.length();
//		String baserUrl = requestURL.substring(0, contextPathIndex);	// http://localhost:8888/codingjoa
//		
//		return new AntPathMatcher().match(baserUrl + "/**", url);
		
		String pattern = ServletUriComponentsBuilder.fromContextPath(request)
				.path("/**")
				.build()
				.toUriString();
		return new AntPathMatcher().match(pattern, url);
	}
	
	@SuppressWarnings("unused")
	private void clearAuthenticationDetails(Authentication authentication) {
		log.info("\t > clear authentication details");
		((UsernamePasswordAuthenticationToken) authentication).setDetails(null);
	}
	
}
