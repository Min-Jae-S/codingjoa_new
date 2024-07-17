package com.codingjoa.controller.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/oauth2")
@RestController
public class TestOAuth2Controller {
	
	@GetMapping("/code/kakao")
	public ResponseEntity<Object> getAuthorizationCode(@RequestParam String code) {
		log.info("## getAuthorizationCode");
		
		// get authorization code from kakao
		log.info("\t > authorization code = {}", code);
		
		// to receive a token, send a new request
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

}
