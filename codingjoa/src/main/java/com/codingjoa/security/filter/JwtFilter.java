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
import com.codingjoa.util.HttpUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private static final String JWT_COOKIE = "ACCESS_TOKEN";
	private final JwtProvider jwtProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));

		String jwt = resolveJwt(request);
		
		if (jwtProvider.isValidJwt(jwt)) {
			log.info("\t > valid JWT, obtain authenticaion from jwt(claims)");
			Authentication authentication = jwtProvider.getAuthentication(jwt); // UsernamePasswordAuthenticationToken

			log.info("\t > temporarily store authenticaion in the security context");
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String resolveJwt(HttpServletRequest request) {
		Cookie cookie = CookieUtils.getCookie(request, JWT_COOKIE);
		return (cookie == null) ? null : cookie.getValue();
	}
	
}
