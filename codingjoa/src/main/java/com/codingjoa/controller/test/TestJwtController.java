package com.codingjoa.controller.test;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@SuppressWarnings("unused")
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
	
	@Value("${security.jwt.secret}")
	private String secretKey;
	
	@Value("${security.jwt.expire}")
	private long expireTime;
	
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
		claims.setIssuer(issuer);
		
		Date now = new Date(System.currentTimeMillis());
		Date exp = new Date(now.getTime() + expireTime);
		
		claims.setIssuedAt(now);
		claims.setExpiration(exp);
		log.info("\t > claims = {}", claims);
		
		// using claims
		String token1 = Jwts.builder()
			.setClaims(claims)
			.compact();
		log.info("\t > token1 = {}", token1);
		
		// not using claims
		String token2 = Jwts.builder()
				.setIssuer(issuer)
				.setIssuedAt(now)
				.setExpiration(exp)
				.compact();
		log.info("\t > token2 = {}", token2);
		log.info("\t > token1 == token2 ? {}", token1.equals(token2));
		
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.message("success")
				.data(Map.of("token", token1))
				.build());
	}
	
}
