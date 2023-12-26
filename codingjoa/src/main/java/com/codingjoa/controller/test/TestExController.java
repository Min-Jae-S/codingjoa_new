package com.codingjoa.controller.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestExController {
	
	@GetMapping("/ex/checked")
	public ResponseEntity<Object> checkedEx() { 
		log.info("## checkedEx");
		return ResponseEntity.ok("success");
	}

	@GetMapping("/ex/unchecked")
	public ResponseEntity<Object> uncheckedEx() { 
		log.info("## uncheckedEx");
		return ResponseEntity.ok("success");
	}
	
}
