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
	
	public static void addCookie(HttpServletResponse response, String name, String value, long expireSeconds) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
				//.domain("localhost")
				.path("/codingjoa")
				.maxAge(Duration.ofSeconds(expireSeconds))
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax") // Strict -> Lax
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		//response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public static void removeCookie(HttpServletResponse response, String name) {
		//addCookie(response, name, null, 0);
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/codingjoa");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	public static void removeCookies(HttpServletRequest request, HttpServletResponse response) {
		getCookies(request).forEach(cookie -> removeCookie(response, cookie.getName()));
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
