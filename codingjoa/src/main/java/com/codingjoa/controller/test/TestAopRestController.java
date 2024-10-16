package com.codingjoa.controller.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/api/aop")
@RestController
public class TestAopRestController {

	@GetMapping("/exception")
	public void triggerExceptionByAjax() {
		log.info("## triggerExceptionByAjax");
		throw new RuntimeException();
	}

	@GetMapping("/exception/filter")
	public void triggerExceptionInFilter() {
		log.info("## triggerExceptionInFilter");
		throw new RuntimeException();
	}
	
	@GetMapping("/exception/interceptor")
	public void triggerExceptionInInterceptor() {
		log.info("## triggerExceptionInInterceptor");
		throw new RuntimeException();
	}
	
	@GetMapping("/exception/controller")
	public void triggerExceptionInController() {
		log.info("## triggerExceptionInController");
		throw new RuntimeException();
	}
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
}
