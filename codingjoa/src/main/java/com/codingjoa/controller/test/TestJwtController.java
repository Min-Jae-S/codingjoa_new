package com.codingjoa.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.service.JwtProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/jwt")
@RestController
public class TestJwtController {

	@Autowired
	private JwtProvider jwtProvider;
	
	@GetMapping("/token")
	public ResponseEntity<Object> createToken(Authentication authentication) {
		log.info("## test1");
		log.info("\t > authentication = {}", authentication);
		
		String token = jwtProvider.createToken(authentication);
		log.info("\t > token = {}", token);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	
}
