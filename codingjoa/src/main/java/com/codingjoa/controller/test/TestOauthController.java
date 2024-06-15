package com.codingjoa.controller.test;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestOauthController {
	
	@GetMapping("/oauth/test1")
	public ResponseEntity<Object> test1() { 
		log.info("## test1");
		return ResponseEntity.ok("success");
	}
	

}
