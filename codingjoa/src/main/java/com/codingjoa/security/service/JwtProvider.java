package com.codingjoa.security.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.entity.Member;
import com.codingjoa.security.dto.UserDetailsDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

	private final String SECRET_KEY;
	private final long VALIDITY_IN_MILLIS;
	private final UserDetailsService userDetailsService;
	
	public JwtProvider(@Value("${security.jwt.secret-key}") String secretKey, 
			@Value("${security.jwt.validity-in-mills}") long validityInMillis, UserDetailsService userDetailsService) {
		this.SECRET_KEY = secretKey;
		this.VALIDITY_IN_MILLIS = validityInMillis;
		this.userDetailsService = userDetailsService;
	}
	
	public String createToken(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Date now = new Date(System.currentTimeMillis());
		Date exp = new Date(now.getTime() + VALIDITY_IN_MILLIS);
		
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setHeader(createHeader())
				.setIssuer(createIssuer())
				.setClaims(createClaims(userDetails))
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(createSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public String createToken(UserDetails userDetails) {
		Date now = new Date(System.currentTimeMillis());
		Date exp = new Date(now.getTime() + VALIDITY_IN_MILLIS);
		
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setHeader(createHeader())
				.setIssuer(createIssuer())
				.setClaims(createClaims(userDetails))
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(createSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Authentication getAuthentication(String token) {
		//return new UsernamePasswordAuthenticationToken
		return null;
	}
	
	public boolean validateToken(String token) {
		return false;
	}
	
	private String createIssuer() {
		return ServletUriComponentsBuilder.fromCurrentContextPath()
				.build()
				.toString();
	}
	
	private Map<String, Object> createHeader() {
		return Map.of("typ", "JWT", "alg", "HS256");
	}
	
	private Map<String, Object> createClaims(UserDetails userDetails) {
		UserDetailsDto userDetailsDto = (UserDetailsDto) userDetails;
		return Map.of("email", userDetailsDto.getMember().getMemberEmail(), "role", userDetailsDto.getMemberRole());
	}
	
	private Key createSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	}
	
}
