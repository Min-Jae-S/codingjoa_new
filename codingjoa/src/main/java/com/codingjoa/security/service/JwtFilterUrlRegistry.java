package com.codingjoa.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class JwtFilterUrlRegistry {
	
	private List<RequestMatcher> includeMatchers = new ArrayList<>();
	
	public void addPatterns(String... antPatterns) {
		addPatterns(null, antPatterns);
	}
	
	public void addPatterns(HttpMethod httpMethod, String... antPatterns) {
		String method = httpMethod == null ? null : httpMethod.toString();
		List<RequestMatcher> matchers = new ArrayList<>();
		for (String pattern : antPatterns) {
			matchers.add(new AntPathRequestMatcher(pattern, method));
		}
	}
}
