package com.codingjoa.controller.test;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.service.JwtProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/jwt")
@RestController
public class TestJwtController {

	/*
	 * @ https://yuma1029.tistory.com/15
	 * @ JWT (Json Web Token)
	 * 		- xxxxx.yyyyy.zzzzz 	// header.payload.signature
	 * 		- header 				// type(typ), algorithm(alg)
	 * 		- payload				// issuer(iss), subject(sub), audience(aud), issued at(iat), expired(exp) [claim]
	 * 		- signature
	 */
	
	@SuppressWarnings("unused")
	@Autowired
	private JwtProvider jwtProvider;
	
	@GetMapping("/create-token")
	public ResponseEntity<Object> createTokean(HttpServletRequest request) {
		log.info("## createToken");
		
		// issuer(iss), subject(sub), audience(aud), issued at(iat), expired(exp) [claim]
		Claims claims = Jwts.claims();
		String issuer = ServletUriComponentsBuilder.fromCurrentContextPath()
				.build()
				.toString();
		log.info("\t > issuer = {}", issuer);
		claims.setIssuer(issuer);
		
		Date now = new Date(System.currentTimeMillis());
		Date now2 = new Date();
		log.info("\t > now = {}", now);
		log.info("\t > now2 = {}", now2);
		
		Date exp = new Date(now.getTime());
		
		claims.setIssuedAt(now);
		claims.setExpiration(exp);
		log.info("\t > claims = {}", claims);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
}
