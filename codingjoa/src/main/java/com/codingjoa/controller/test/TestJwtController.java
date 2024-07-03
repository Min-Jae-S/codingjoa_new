package com.codingjoa.controller.test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
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
		String key = Encoders.BASE64.encode(createKey(SECRET_KEY).getEncoded());
		log.info("\t > key = {}", key);
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/token")
	public ResponseEntity<Object> token() {
		log.info("## token");
		
		String username = "smj20228";
		UserDetailsDto userDetailsDto = (UserDetailsDto) userDetailsService.loadUserByUsername(username);
		
		String token = jwtProvider.createToken(userDetailsDto.getMember());
		log.info("\t > token = {}", token);
		
		Key key = createKey(SECRET_KEY);
		Jwt jwt = Jwts.parserBuilder().setSigningKey(key).build().parse(token);
		log.info("\t > header = {}", jwt.getHeader());
		log.info("\t > body = {}", jwt.getBody());
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.message("success")
				.data(Map.of("token", token))
				.build());
	}
	
	private Key createKey(String secretKey) {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}
	
}
