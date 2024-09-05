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
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/remove")
	public ResponseEntity<Object> remove(HttpServletRequest request, HttpServletResponse response) {
		log.info("## remove");
		CookieUtils.removeCookies(request, response);
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/add/cookie1")
	public ResponseEntity<Object> addCookie1(HttpServletRequest request, HttpServletResponse response) {
		log.info("## addCookie1");
		CookieUtils.addCookie(request, response, "TEST", "abcd", 300); // 5 mins
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/add/cookie2")
	public ResponseEntity<Object> addCookie2(HttpServletRequest request, HttpServletResponse response) {
		log.info("## addCookie2");
		addCookie(request, response, "TEST", "ABCD"); // 10 mins
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/add/cookies")
	public ResponseEntity<Object> addCookies(HttpServletRequest request, HttpServletResponse response) {
		log.info("## addCookies");
		for (int i = 1; i <= 3; i++) {
			addCookie(request, response, "TEST" + i, createCookieValue());
		}
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	private void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(request.getContextPath() + "/");
		cookie.setMaxAge((int) Duration.ofMinutes(10).toSeconds());
		response.addCookie(cookie);
	}
	
	private String createCookieValue() {
		return  UUID.randomUUID().toString().replace("-", "");
	}
	
}
