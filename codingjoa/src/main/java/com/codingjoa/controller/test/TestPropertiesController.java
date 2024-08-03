package com.codingjoa.controller.test;

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
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() throws Exception {
		log.info("## test1");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

}
