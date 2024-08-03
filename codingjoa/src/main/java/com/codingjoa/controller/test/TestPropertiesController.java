package com.codingjoa.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/properties")
@RestController
public class TestPropertiesController {
	
	@Autowired
	private Environment env;
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() throws Exception {
		log.info("## test1");
		
		String param1 = env.getProperty("security.param1");
		log.info("\t > param1 = {}", param1);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

}
