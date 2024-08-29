package com.codingjoa.util;

import java.time.Duration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class CookieUtils {
	
	public static void saveCookie(HttpServletResponse response, String name, String value) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
				.domain("localhost")
				.path("/")
				.maxAge(Duration.ofMinutes(10))
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax")
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public void removeCookie(HttpServletResponse response, String name, String value) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
				.domain("localhost")
				.path("/")
				.maxAge(Duration.ofMinutes(10))
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax")
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
}
