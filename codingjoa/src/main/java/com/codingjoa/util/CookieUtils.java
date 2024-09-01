package com.codingjoa.util;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class CookieUtils {
	
	public static void addCookie(HttpServletResponse response, String name, String value, long expireSeconds) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
				.domain("localhost")
				.path("/")
				.maxAge(Duration.ofSeconds(expireSeconds))
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax") // Strict -> Lax
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie cookie = getCookie(request, name);
		cookie.setMaxAge(0);
		response.addHeader(name, cookie.toString());
	}
	
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
	    if (cookies == null || cookies.length == 0) {
	        return null;
	    }
		
	    return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .findFirst()
                .orElse(null);
	}
	
	public static List<String> getCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		
		return Arrays.stream(cookies)
				.map(cookie -> cookie.getName())
				.collect(Collectors.toList());
	}
}
