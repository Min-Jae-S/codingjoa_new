package com.codingjoa.util;

import java.time.Duration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class CookieUtils {
	
	public static void saveCookie(HttpServletResponse response, String name, String value, long cookieExpireSeconds) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
				.domain("localhost")
				.path("/")
				.maxAge(Duration.ofSeconds(cookieExpireSeconds))
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax") // Strict -> Lax
				.build();
		//response.setHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public void removeCookie(HttpServletRequest request, HttpServletResponse response, String value) {
		
	}
	
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		
		return null;
	}
}
