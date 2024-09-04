package com.codingjoa.util;

import java.nio.charset.Charset;
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
		return encode(currentUrl, StandardCharsets.UTF_8);
	}
	
	// AuthenticationEntryPointImpl
	public static String buildLoginUrl(HttpServletRequest request) {
		return ServletUriComponentsBuilder.fromContextPath(request)
				.path("/login")
				.queryParam("continue", buildCurrentUrl(request))
				.build()
				.toUriString();
	}
	
	// LoginSuccessHandler, OAuth2LoginSuccessHandler, LogoutSuccessHandler
	public static String resolveContinueUrl(String url, HttpServletRequest request) {
		String continueUrl;
		if (isAuthorizedUrl(url, request)) {
			continueUrl = url;
			log.info("\t > authorized URL provided, using this URL: {}", FormatUtils.formatString(continueUrl));
		} else {
			continueUrl = ServletUriComponentsBuilder.fromContextPath(request)
					.path("/")
					.build()
					.toUriString();
			log.info("\t > missing or unauthorized URL provided, using default URL: {}", FormatUtils.formatString(continueUrl));
		}
		
		return continueUrl;
	}
	
	private static boolean isAuthorizedUrl(String url, HttpServletRequest request) {
		return StringUtils.hasText(url) && isUrlMatchingPattern(url, request);
	}
	
	private static boolean isUrlMatchingPattern(String url, HttpServletRequest request) {
		String contextPattern = ServletUriComponentsBuilder.fromContextPath(request)
				.path("/**")
				.build(false)
				.toUriString();
		return new AntPathMatcher().match(contextPattern, url);
	}
	
	private static String encode(String url, Charset charset) {
		return org.springframework.web.util.UriUtils.encode(url, charset);
	}
}
