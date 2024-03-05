package com.codingjoa.controller.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestRestApiController {

	@GetMapping("/rest-api/test1")
	public ResponseEntity<Object> test1() { 
		log.info("## test1");
		return ResponseEntity.ok("success");
	}

	@GetMapping("/rest-api/test2")
	public ResponseEntity<Object> test2() { 
		log.info("## test2");
		return ResponseEntity.ok("success");
	}
	
}
