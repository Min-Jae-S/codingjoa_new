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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtProvider jwtProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("## {}.doFilterInternal", this.getClass().getSimpleName());
		
		String jwt = resolveJwt(request);
		
		if (jwtProvider.isValidJwt(jwt)) {
			log.info("\t > valid JWT, setting authenticaion in the security context");
			Authentication authentication = jwtProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {
			log.info("\t > missing or invalid JWT, no authenticaion set in the security context");
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String resolveJwt(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				if ("access_token".equals(name)) {
					return cookie.getValue();
				}
			}
		}
		
		return null;
	}

//	private String resolveJwt(HttpServletRequest request) {
//		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//		if (header != null && header.startsWith("Bearer ")) {
//			return header.split(" ")[1];
//		}
//		
//		return null;
//	}
	
	
}
