package com.codingjoa.controller.test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.response.SuccessResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@PropertySource("/WEB-INF/properties/security.properties")
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
	
	private final Key KEY;
	private final long VALIDITY_IN_MILLIS;
	private final UserDetailsService userDetailsService;
	
	public TestJwtController(@Value("${security.jwt.secret-key}") String secretKey, 
			@Value("${security.jwt.validity-in-mills}") long validityInMillis, UserDetailsService userDetailsService) {
		this.KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.VALIDITY_IN_MILLIS = validityInMillis;
		this.userDetailsService = userDetailsService;
	}
	
	@GetMapping("/key")
	public ResponseEntity<Object> getKey() {
		log.info("## getKey");
		String key = Encoders.BASE64.encode(KEY.getEncoded());
		log.info("\t > key = {}", key);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/create-token")
	public ResponseEntity<Object> createTokean(HttpServletRequest request) {
		log.info("## createToken");
		Date now = new Date(System.currentTimeMillis());
		Date exp = new Date(now.getTime() + VALIDITY_IN_MILLIS);
		
		// issuer(iss), subject(sub), audience(aud), issued at(iat), expired(exp) [claim]
		Claims claims = Jwts.claims();
		String issuer = ServletUriComponentsBuilder.fromCurrentContextPath()
				.build()
				.toString();
		claims.setIssuer(issuer);
		claims.setIssuedAt(now);
		claims.setExpiration(exp);
		
		String token1 = Jwts.builder()
			.setClaims(claims)
			.signWith(KEY, SignatureAlgorithm.HS256)
			.compact();
		log.info("\t > token1 = {}", token1);
		
		String token2 = Jwts.builder()
				.setIssuer(issuer)
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(KEY, SignatureAlgorithm.HS256)
				.compact();
		log.info("\t > token2 = {}", token2);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.message("success")
				.data(Map.of("token1", token1, "token2", token2))
				.build());
	}
	
}
