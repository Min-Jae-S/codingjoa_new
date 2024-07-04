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
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	 * @ Json Web Token (JWT)
	 * 	xxxxx.yyyyy.zzzzz 	// header.payload.signature
	 * 	header 				// type(typ), algorithm(alg)
	 * 	payload				// issuer(iss), subject(sub), audience(aud), issued at(iat), expired(exp) - claims
	 * 	signature
	 */

	private final Key signingKey;
	private final long VALIDITY_IN_MILLIS;
	private final UserDetailsService userDetailsService;
	
	public JwtProvider(@Value("${security.jwt.secret-key}") String secretKey, 
			@Value("${security.jwt.validity-in-mills}") long validityInMillis, UserDetailsService userDetailsService) {
		this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.VALIDITY_IN_MILLIS = validityInMillis;
		this.userDetailsService = userDetailsService;
	}
	
	/*
	 * header - typ, alg
	 * claims - sub, iss, iat, exp, email, role
	 */
	public String createToken(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return Jwts.builder()
				.setHeader(createHeader()) 
				.setClaims(createClaims(userDetails))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
	}

	public Authentication getAuthentication(String token) {
		String username = parseClaims(token).getSubject();
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
	
	/*
	 * UnsupportedJwtException - if the claimsJws argument does not represent an Claims JWS ex) no claims
	 * MalformedJwtException - if the claimsJws string is not a valid JWS
	 * SignatureException - if the claimsJws JWS signature validation fails
	 * ExpiredJwtException - if the specified JWT is a Claims JWT and the Claims has an expiration timebefore the time this method is invoked.
	 * IllegalArgumentException - if the claimsJws string is null or empty or only whitespace
	 */
	public boolean validateToken(String token) {
		try {
			Claims claims = parseClaims(token);
			if (claims.getExpiration() == null) {
				throw new IllegalArgumentException("'exp' is required");
			}
			
			String username = claims.getSubject();
			if (!StringUtils.hasText(username)) {
				throw new IllegalArgumentException("'sub' is required");
			}
			
			return true;
			//return !claims.getExpiration().before(new Date(System.currentTimeMillis()));
		} catch (Exception e) { 
			log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
			return false;
			//throw e;
		}
	}
	
	private Claims parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
	}
	
	private Map<String, Object> createHeader() {
		return Map.of("typ", "JWT", "alg", "HS256");
	}
	
	private Map<String, Object> createClaims(UserDetails userDetails) {
		UserDetailsDto userDetailsDto = (UserDetailsDto) userDetails;
		Date now = new Date(System.currentTimeMillis());
		Date exp = new Date(now.getTime() + VALIDITY_IN_MILLIS);
		
		Claims claims = Jwts.claims()
				.setSubject(userDetailsDto.getUsername())
				.setIssuer(ServletUriComponentsBuilder.fromCurrentContextPath().build().toString())
				.setIssuedAt(now)
				.setExpiration(exp);
		claims.put("role", userDetailsDto.getMemberRole());
		
		return claims;
	}
	
}
