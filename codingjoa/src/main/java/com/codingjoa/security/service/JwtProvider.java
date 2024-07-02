package com.codingjoa.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
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

	private final String secretKey;
	private final long expireTime;
	private final UserDetailsService userDetailsService;
	
	public JwtProvider(@Value("${security.jwt.secret}") String secretKey,
			@Value("${security.jwt.expire}") long expireTime, UserDetailsService userDetailsService) {
		this.secretKey = secretKey;
		this.expireTime = expireTime;
		this.userDetailsService = userDetailsService;
	}
	
	public String createToken(Authentication authentication) {
		return Jwts.builder().compact();
	}
	
	public Authentication getAuthentication(String token) {
		//return new UsernamePasswordAuthenticationToken
		return null;
	}

	
}
