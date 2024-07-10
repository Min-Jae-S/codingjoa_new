package com.codingjoa.controller.test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/cookie-session")
@RestController
public class TestCookieSessionController {

	@GetMapping("/test1")
	public ResponseEntity<Object> test1(HttpServletRequest request, HttpServletResponse response) {
		log.info("## test1");
		for (Cookie cookie : request.getCookies()) {
			log.info("\t > cookie name = {}, cookie value = {}", cookie.getName(), cookie.getValue());
		}
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test2")
	public ResponseEntity<Object> test2(@CookieValue("JSESSIONID") String value1, 
			@CookieValue(name = "jsessionid", required = false) String value2) {
		log.info("## test2");
		log.info("\t > value1 = {}", value1);
		log.info("\t > value2 = {}", value2);
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/create-cookie")
	public ResponseEntity<Object> createCookie() {
		log.info("## createCookie");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
}
