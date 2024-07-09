package com.codingjoa.controller.test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Object> test1(HttpServletRequest request) {
		log.info("## test1");
		for (Cookie cookie : request.getCookies()) {
			log.info("\t > name = {}, value = {}", cookie.getName(), cookie.getValue());
		}
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/create-cookie")
	public ResponseEntity<Object> createCookie() {
		log.info("## createCookie");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
}
