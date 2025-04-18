package com.codingjoa.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.util.CookieUtils;
import com.codingjoa.util.RequestUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private static final String JWT_COOKIE = "ACCESS_TOKEN";
	private final JwtProvider jwtProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", RequestUtils.getRequestLine(request));

		String jwt = extractJwt(request);
		
		if (jwtProvider.isValidJwt(jwt)) {
			log.info("\t > valid JWT, obtain authenticaion from jwt(claims)");
			Authentication authentication = jwtProvider.getAuthentication(jwt); // UsernamePasswordAuthenticationToken

			log.info("\t > temporarily store authenticaion in the SecurityContext");
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String extractJwt(HttpServletRequest request) {
		if (RequestUtils.isRestApiRequest(request)) {
			log.info("\t > rest-api request detected, extract JWT from Authorization header");
			return extractJwtFromHeader(request);
		} else {
			log.info("\t > normal request detected, extract JWT from cookie");
			return extractJwtFromCookie(request);
		}
	}
	
	private String extractJwtFromHeader(HttpServletRequest request) {
		return null;
	}
	
	private String extractJwtFromCookie(HttpServletRequest request) {
		Cookie cookie = CookieUtils.getCookie(request, JWT_COOKIE);
		return (cookie == null) ? null : cookie.getValue();
	}
	
}
