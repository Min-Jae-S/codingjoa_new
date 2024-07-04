package com.codingjoa.security.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
@PropertySource("/WEB-INF/properties/security.properties")
@Component
public class JwtProvider {
	
	/*
	 * @ https://yuma1029.tistory.com/15
	 * @ JWT (Json Web Token)
	 * 		- xxxxx.yyyyy.zzzzz 	// header.payload.signature
	 * 		- header 				// type(typ), algorithm(alg)
	 * 		- payload				// issuer(iss), subject(sub), audience(aud), issued at(iat), expired(exp) - claims
	 * 		- signature
	 */

	private final Key signingKey;
	private final long VALIDITY_IN_MILLIS;
	
	@SuppressWarnings("unused")
	private final UserDetailsService userDetailsService;
	
	public JwtProvider(@Value("${security.jwt.secret-key}") String secretKey, 
			@Value("${security.jwt.validity-in-mills}") long validityInMillis, UserDetailsService userDetailsService) {
		this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.VALIDITY_IN_MILLIS = validityInMillis;
		this.userDetailsService = userDetailsService;
	}
	
	public String createToken(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return Jwts.builder()
				.setHeader(createHeader()) 				// typ, alg
				.setClaims(createClaims(userDetails)) 	// sub, iss, iat, exp, email, role
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
	}

	public String createToken(UserDetails userDetails) {
		return Jwts.builder()
				.setHeader(createHeader()) 				// typ, alg
				.setClaims(createClaims(userDetails)) 	// sub, iss, iat, exp, email, role
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(token);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // isAuthenticated = true
	}
	
	public boolean validateToken(String token) {
		log.info("\t > validate token");
		try {
			Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) { 
			// ExpiredJwtException >> ClaimJwtException >> JwtException 
			// MalformedJwtException, UnsupportedJwtException >> JwtException
			// NullPointerException
			log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
			throw e;
			//return false;
		}
	}
	
	private Map<String, Object> createHeader() {
		return Map.of("typ", "JWT", "alg", "HS256");
	}
	
	private Map<String, Object> createClaims(UserDetails userDetails) {
		UserDetailsDto userDetailsDto = (UserDetailsDto) userDetails;
		Member member = userDetailsDto.getMember();
		
		Date now = new Date(System.currentTimeMillis());
		Date exp = new Date(now.getTime() + VALIDITY_IN_MILLIS);
		
		Claims claims = Jwts.claims()
				.setSubject(member.getMemberId())
				.setIssuer(ServletUriComponentsBuilder.fromCurrentContextPath().build().toString())
				.setIssuedAt(now)
				.setExpiration(exp);
		claims.put("email", member.getMemberEmail());
		claims.put("role", userDetailsDto.getMemberRole());
		
		return claims;
	}
	
}
