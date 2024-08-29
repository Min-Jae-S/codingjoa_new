package com.codingjoa.util;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UriUtils {

	// TopMenuInterceptor 
	public static String buildCurrentUrl(HttpServletRequest request) {
		String currentUrl = UrlUtils.buildFullRequestUrl(request);
		return encode(currentUrl);
	}
	
	// AuthenticationEntryPointImpl
	public static String buildLoginUrl(HttpServletRequest request) {
		return ServletUriComponentsBuilder.fromContextPath(request)
				.path("/login")
				.queryParam("continue", buildCurrentUrl(request))
				.build()
				.toUriString();
	}
	
	public static String resolveUrl(String url, HttpServletRequest request) {
		if (!isAuthorizedUrl(url, request)) {
			log.info("\t > missing or unauthorized url provided, default url will be used");
			return ServletUriComponentsBuilder.fromContextPath(request)
					.path("/")
					.build()
					.toUriString();
		} else {
			log.info("\t > authorized url provided, this url will be used");
			return url;
		}
	}
	
	private static String encode(String url) {
		return org.springframework.web.util.UriUtils.encode(url, StandardCharsets.UTF_8);
	}
	
	private static boolean isAuthorizedUrl(String url, HttpServletRequest request) {
		if (!StringUtils.hasText(url)) {
			return false;
		}
		
		String pattern = ServletUriComponentsBuilder.fromContextPath(request)
				.path("/**")
				.build(false)
				.toUriString();
		return new AntPathMatcher().match(pattern, url);
	}
}
