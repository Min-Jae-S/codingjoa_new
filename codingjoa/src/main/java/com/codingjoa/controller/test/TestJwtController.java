package com.codingjoa.controller.test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.service.JwtProvider;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/jwt")
@RequiredArgsConstructor
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
	
	private final String SECRET_KEY = "JsonWebTokenSecretKeyForJwtAuthenticationInSpringSecurity";
	private final JwtProvider jwtProvider;
	private final UserDetailsService userDetailsService;
	
	@GetMapping("/key")
	public ResponseEntity<Object> key() {
		log.info("## key");
		String key = Encoders.BASE64.encode(createSigningKey(SECRET_KEY).getEncoded());
		log.info("\t > key = {}", key);
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/token")
	public ResponseEntity<Object> token() {
		log.info("## token");

		UserDetails userDetails = userDetailsService.loadUserByUsername("smj20228");
		String token = jwtProvider.createToken(userDetails);
		
		@SuppressWarnings("rawtypes")
		Jwt jwt = Jwts.parserBuilder().setSigningKey(createSigningKey(SECRET_KEY)).build().parse(token);
		log.info("\t > parsed jwt = {}", jwt.getClass().getSimpleName());
		
		log.info("\t > header = {}", jwt.getHeader());
		log.info("\t > body = {}", jwt.getBody());
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.message("success")
				.data(Map.of("token", token))
				.build());
	}
	
	private Key createSigningKey(String secretKey) {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}
	
}
