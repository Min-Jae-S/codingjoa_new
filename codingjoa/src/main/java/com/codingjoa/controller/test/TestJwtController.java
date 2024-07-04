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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
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
	 * 		- payload				// issuer(iss), subject(sub), audience(aud), issued at(iat), expired(exp) - claims
	 * 		- signature
	 */
	
	private final String SECRET_KEY = "JsonWebTokenSecretKeyForJwtAuthenticationInSpringSecurity";
	private final Key signingKey; 
	private final JwtProvider jwtProvider;
	private final UserDetailsService userDetailsService;
	
	public TestJwtController(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
		this.signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));;
		this.jwtProvider = jwtProvider;
		this.userDetailsService = userDetailsService;
	}

	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		String key = Encoders.BASE64.encode(signingKey.getEncoded()); 
		log.info("\t > key = {}", key); // SnNvbldlYlRva2VuU2VjcmV0S2V5Rm9ySnd0QXV0aGVudGljYXRpb25JblNwcmluZ1NlY3VyaXR5
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");

		UserDetails userDetails = userDetailsService.loadUserByUsername("smj20228");
		String token = jwtProvider.createToken(userDetails);
		log.info("\t > JWT = {}", token);
		
		Jws<Claims> jws = Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token);
		log.info("\t > JWT header = {}", jws.getHeader().keySet());
		log.info("\t > JWT body = {}", jws.getBody().keySet());
		log.info("\t > JWT signature = {}", jws.getSignature());

		return ResponseEntity.ok(SuccessResponse.builder()
				.message("success")
				.data(Map.of("token", token))
				.build());
	}

	@GetMapping("/test3")
	public ResponseEntity<Object> test3() {
		log.info("## test3");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
}
