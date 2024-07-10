package com.codingjoa.controller.test;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;
import com.mchange.v3.decode.Decoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/cookie-session")
@RestController
public class TestCookieSessionController {

	@GetMapping("/test1")
	public ResponseEntity<Object> test1(HttpServletRequest request, HttpServletResponse response) {
		log.info("## test1");
		for (Cookie cookie : request.getCookies()) {
			log.info("\t > {} = {}, httpOnly = {}", cookie.getName(), cookie.getValue(), cookie.isHttpOnly());
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
	public ResponseEntity<Object> createCookie(HttpServletResponse response) {
		log.info("## createCookie");
		Cookie cookie1 = new Cookie("cookie1", createRandomUUID());
		Cookie cookie2 = new Cookie("cookie2", createRandomUUID());
		Cookie cookie3 = new Cookie("cookie3", createRandomUUID());
		cookie3.setMaxAge(3600);
		
		Cookie cookie4 = new Cookie("cookie4", createRandomUUID());
		cookie4.setHttpOnly(true);
		
		Cookie[] cookies = new Cookie[] {cookie1, cookie2, cookie3, cookie4};
		for (Cookie cookie : cookies) {
			log.info("\t > {} = {}, httpOnly = {}", cookie.getName(), cookie.getValue(), cookie.isHttpOnly());
		}
		
		response.addCookie(cookie1);
		response.addCookie(cookie2);
		response.addCookie(cookie3);
		response.addCookie(cookie4);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	private String createRandomUUID() {
		return  UUID.randomUUID().toString().replace("-", "");
	}
	
}
