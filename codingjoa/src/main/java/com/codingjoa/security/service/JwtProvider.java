package com.codingjoa.security.service;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@PropertySource("/WEB-INF/properties/security.properties")
@Component
public class JwtProvider {
	
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
	
	public JwtProvider(@Value("${security.jwt.secret-key}") String secretKey, 
			@Value("${security.jwt.validity-in-mills}") long validityInMillis, UserDetailsService userDetailsService) {
		this.KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		//this.KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.VALIDITY_IN_MILLIS = validityInMillis;
		this.userDetailsService = userDetailsService;
	}
	
	public String createToken(Authentication authentication) {
		return Jwts.builder()
				.signWith(KEY, SignatureAlgorithm.HS256) // HMAC + SHA256
				.compact();
	}
	
	public Authentication getAuthentication(String token) {
		//return new UsernamePasswordAuthenticationToken
		return null;
	}
	
	
}
