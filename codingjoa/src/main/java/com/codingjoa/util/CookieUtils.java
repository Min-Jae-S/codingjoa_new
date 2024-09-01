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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieUtils {
	
	public static void addCookie(HttpServletResponse response, String name, String value, long expireSeconds) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
				//.domain("localhost")
				.path("/")
				.maxAge(Duration.ofSeconds(expireSeconds))
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax") // Strict -> Lax
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		log.info("## removeCookie");
		
		Cookie cookie = getCookie(request, name);
		if (cookie != null) {
			log.info("\t > cookie exist : {} {}", cookie.getName(), cookie.getValue());
			addCookie(response, name, null, 0);
		}
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
			return Collections.emptyList();
		}
		
		return Arrays.stream(cookies)
				.map(cookie -> cookie.getName())
				.collect(Collectors.toList());
	}
}
