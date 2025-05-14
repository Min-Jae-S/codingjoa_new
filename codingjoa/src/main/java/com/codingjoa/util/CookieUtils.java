package com.codingjoa.util;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class CookieUtils {
	
	// a private constructor to prevent instantiation
	private CookieUtils() {}
	
	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, long expireSeconds) {
		// cookiePath in CookieClearingLogoutHandler
		String cookiePath = request.getContextPath() + "/";
		ResponseCookie cookie = ResponseCookie.from(name, value)
				//.domain("localhost")
				.path(cookiePath) 
				.maxAge(Duration.ofSeconds(expireSeconds))
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax") // Strict -> Lax
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		addCookie(request, response, name, null, 0);
	}
	
	public static void removeCookies(HttpServletRequest request, HttpServletResponse response) {
		getCookies(request).forEach(cookie -> removeCookie(request, response, cookie.getName()));
	}
	
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
	    if (cookies == null || cookies.length == 0) {
	        return null;
	    }
		
	    return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .orElse(null);
	}
	
	public static List<Cookie> getCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return Collections.emptyList();
		}
		
		return Arrays.stream(cookies)
				.collect(Collectors.toList());
	}
	
	public static List<String> getCookieNames(HttpServletRequest request) {
		return getCookies(request).stream()
				.map(cookie -> cookie.getName())
				.collect(Collectors.toList());
	}
}
