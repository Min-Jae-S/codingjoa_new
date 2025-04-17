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
import com.codingjoa.util.RequestUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtMathcerFilter extends OncePerRequestFilter {
	
	private final JwtProvider jwtProvider;
	private List<RequestMatcher> includeMatchers = new ArrayList<>();
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", RequestUtils.getRequestLine(request));
		
		String jwt = resolveJwt(request);
		
		if (jwtProvider.isValidJwt(jwt)) {
			Authentication authentication = jwtProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.info("\t > valid JWT, setting authenticaion in the security context");
		} else {
			log.info("\t > missing or invalid JWT, no authenticaion set in the security context");
		}
		
		filterChain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		log.info("## {}.shouldNotFilter", this.getClass().getSimpleName());
		boolean matchesIncludePattern = includeMatchers.stream().anyMatch(matcher -> matcher.matches(request));
		
		if (matchesIncludePattern) {
			log.info("\t > enter into {} : {}", this.getClass().getSimpleName(), RequestUtils.getRequestLine(request));
		} else {
			log.info("\t > no enter into {} : {}", this.getClass().getSimpleName(), RequestUtils.getRequestLine(request));
		}
		
		return !matchesIncludePattern;
	}
	
	private String resolveJwt(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null && header.startsWith("Bearer ")) {
			return header.split(" ")[1];
		}
		
		return null;
	}
	
	public void addIncludeMatchers(String... antPatterns) {
		addIncludeMatchers(null, antPatterns);
	}
	
	public void addIncludeMatchers(HttpMethod httpMethod, String... antPatterns) {
		String method = httpMethod == null ? null : httpMethod.toString();
		for (String pattern : antPatterns) {
			includeMatchers.add(new AntPathRequestMatcher(pattern, method));
		}
	}
	
}
