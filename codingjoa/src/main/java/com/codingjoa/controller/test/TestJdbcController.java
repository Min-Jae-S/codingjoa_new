package com.codingjoa.controller.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestJdbcController {
	
	@GetMapping("/jdbc/test1")
	public ResponseEntity<Object> jdbcTest1() { 
		log.info("## jdbcTest1");
		return ResponseEntity.ok("success");
	}

	@GetMapping("/jdbc/test2")
	public ResponseEntity<Object> jdbcTest2() { 
		log.info("## jdbcTest2");
		return ResponseEntity.ok("success");
	}
	
}
