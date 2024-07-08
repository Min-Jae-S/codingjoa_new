package com.codingjoa.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codingjoa.security.service.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtProvider jwtProvider;
	private List<RequestMatcher> includeMatchers = new ArrayList<>();
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("## {}", this.getClass().getSimpleName());
		
		String token = resolveToken(request);
		
		if (jwtProvider.validateToken(token)) {
			log.info("\t > valid JWT, setting authenticaion in the security context");
			Authentication authentication = jwtProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {
			log.info("\t > missing or invalid JWT, no authenticaion set in the security context");
		}
		
		filterChain.doFilter(request, response);
	}
	
	public void addIncludeMathers(String... antPatterns) {
		addIncludeMathers(null, antPatterns);
	}
	
	public void addIncludeMathers(HttpMethod httpMethod, String... antPatterns) {
		String method = httpMethod == null ? null : httpMethod.toString();
		for (String pattern : antPatterns) {
			includeMatchers.add(new AntPathRequestMatcher(pattern, method));
		}
	}
	
	private String resolveToken(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null && header.startsWith("Bearer ")) {
			return header.split(" ")[1];
		}
		
		return null;
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		log.info("## {}.shouldNotFilter", this.getClass().getSimpleName());
		boolean matchesIncludePattern = includeMatchers.stream().anyMatch(matcher -> matcher.matches(request));
		if (matchesIncludePattern) {
			log.info("\t > enter into JwtFilter");
		} else {
			log.info("\t > no enter into JwtFilter");
		}
		
		return !matchesIncludePattern;
	}
}
