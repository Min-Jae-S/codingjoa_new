package com.codingjoa.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckAuthenticationFilter extends OncePerRequestFilter {
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > URI = {}", getFullURL(request));
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			log.info("\t > authentication = {}", auth);
		} else {
			log.info("\t > authentication = {}, details = {}", auth.getClass().getSimpleName(), auth.getDetails());
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String getFullURL(HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();
		
		if (queryString == null) {
			return requestURL.toString();
		} else {
			//return requestURL.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
			return requestURL.append('?').append(queryString).toString();
		}
	}
}
