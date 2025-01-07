package com.codingjoa.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UriUtils {
	
	private static final List<String> disallowedPaths = List.of("/login/**", "/error");
	
	// TopMenuInterceptor 
	public static String buildCurrentUrl(HttpServletRequest request) {
		String currentUrl = UrlUtils.buildFullRequestUrl(request);
		return encode(currentUrl, StandardCharsets.UTF_8);
	}
	
	// AuthenticationEntryPointImpl
	public static String buildLoginUrl(HttpServletRequest request) {
		return getLoginUrlBuilder(request)
				.queryParam("continue", buildCurrentUrl(request))
				.build()
				.toUriString();
	}
	
	// OAuth2LoginFailureHandler, MemberController.join
	public static String buildLoginUrl(HttpServletRequest request, String continueUrl) {
		return getLoginUrlBuilder(request)
				.queryParam("continue", continueUrl)
				.build()
				.toUriString();
	}
	
	private static UriComponentsBuilder getLoginUrlBuilder(HttpServletRequest request) {
		return ServletUriComponentsBuilder.fromContextPath(request).path("/login");
	}
	
	// LoginSuccessHandler, OAuth2LoginSuccessHandler, LogoutSuccessHandlerImpl, AccessDeniedHandlerImpl
	public static String resolveContinueUrl(String url, HttpServletRequest request) {
		log.info("## resolveContinueUrl");
		log.info("\t > url = {}", url);
		
		String continueUrl;
		if (isAllowedUrl(url, request)) {
			continueUrl = url;
			log.info("\t > allowed URL provided, using this URL: {}", FormatUtils.formatString(continueUrl));
		} else {
			continueUrl = ServletUriComponentsBuilder.fromContextPath(request)
					.path("/")
					.build()
					.toUriString();
			log.info("\t > missing or disallowed URL provided, using default URL: {}", FormatUtils.formatString(continueUrl));
		}
		
		return continueUrl;
	}
	
	private static boolean isAllowedUrl(String url, HttpServletRequest request) {
		return StringUtils.hasText(url) && isAllowedPath(url, request);
	}
	
	private static boolean isAllowedPath(String url, HttpServletRequest request) {
		AntPathMatcher matcher = new AntPathMatcher();

		// http://localhost:8888/codingjoa
		String contextPattern = ServletUriComponentsBuilder.fromContextPath(request)
				.path("/**")
				.build(false)
				.toUriString();
		log.info("\t > contextPattern = {}", contextPattern);
		
		if (!matcher.match(contextPattern, url)) {
			return false;
		}
		
		return disallowedPaths.stream().noneMatch(path -> { 
			String disallowedPattern = ServletUriComponentsBuilder.fromContextPath(request)
					.path(path)
					.build(false)
					.toUriString();
			log.info("\t > disallowedPattern = {}", disallowedPattern);
			return matcher.match(disallowedPattern, url);
		});
	}
	
	private static String encode(String url, Charset charset) {
		return org.springframework.web.util.UriUtils.encode(url, charset);
	}
}
