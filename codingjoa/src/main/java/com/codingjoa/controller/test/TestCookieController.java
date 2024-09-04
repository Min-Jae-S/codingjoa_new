package com.codingjoa.controller.test;

import java.time.Duration;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.session.CookieWebSessionIdResolver;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.util.CookieUtils;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequestMapping("/test/cookie")
@RestController
public class TestCookieController {

	@GetMapping("/check")
	public ResponseEntity<Object> check(HttpServletRequest request, HttpServletResponse response) {
		log.info("## check");
		log.info("\t > cookies = {}", CookieUtils.getCookieNames(request));
		
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) {
			log.info("\t > no cookies");
		} else {
			for (Cookie cookie : cookies) {
				log.info("\t > name = {}, value = {}", cookie.getName(), cookie.getValue());
			}
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/remove")
	public ResponseEntity<Object> remove(HttpServletRequest request, HttpServletResponse response) {
		log.info("## remove");
		CookieUtils.removeCookies(request, response);
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test1")
	public ResponseEntity<Object> test1(HttpServletRequest request, HttpServletResponse response) {
		log.info("## test1");
		addCookie1(response, "TEST", "1234");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test2")
	public ResponseEntity<Object> test2(HttpServletRequest request, HttpServletResponse response) {
		log.info("## test2");
		addCookie2(response, "TEST", "5678");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	private void addCookie1(HttpServletResponse response, String name, String value) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
				.path("/codingjoa")
				.maxAge(Duration.ofMinutes(5))
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax") // Strict -> Lax
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

	private void addCookie2(HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/codingjoa");
		cookie.setMaxAge((int) Duration.ofMinutes(10).toSeconds());
		response.addCookie(cookie);
	}
	
}
