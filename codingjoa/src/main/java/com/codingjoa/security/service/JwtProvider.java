package com.codingjoa.security.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import io.jsonwebtoken.Jws;
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
	private final long validityInMillis; // 1000 * 60 * 60 (1 hour)
	private final UserDetailsService userDetailsService;
	
	public JwtProvider(@Value("${security.jwt.secret-key}") String secretKey, 
			@Value("${security.jwt.validity-in-mills}") long validityInMillis, UserDetailsService userDetailsService) {
		this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.validityInMillis = validityInMillis;
		this.userDetailsService = userDetailsService;
	}
	
	/*
	 * header - typ, alg
	 * claims - sub, iss, iat, exp, email, role
	 */
	public String createJwt(Authentication authentication, HttpServletRequest request) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return Jwts.builder()
				.setHeader(createHeader()) 
				.setClaims(createClaims(userDetails, request))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
	}

	// https://velog.io/@tmdgh0221/Spring-Security-%EC%99%80-OAuth-2.0-%EC%99%80-JWT-%EC%9D%98-%EC%BD%9C%EB%9D%BC%EB%B3%B4
	// check comment: to fully leverage the advantages of using JWT, it's preferable to avoid database access during the verification process.
	public Authentication getAuthentication(String jwt) {
		String username = parseJwt(jwt).getBody().getSubject();
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authRequest = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authRequest.setDetails("JwtFilter");
		return authRequest;
	}
	
	/*
	 * UnsupportedJwtException - if the claimsJws argument does not represent an Claims JWS ex) no claims
	 * MalformedJwtException - if the claimsJws string is not a valid JWS
	 * SignatureException - if the claimsJws JWS signature validation fails
	 * ExpiredJwtException - if the specified JWT is a Claims JWT and the Claims has an expiration timebefore the time this method is invoked.
	 * IllegalArgumentException - if the claimsJws string is null or empty or only whitespace
	 */
	public boolean isValidJwt(String jwt) {
		try {
			Jws<Claims> jws = parseJwt(jwt);
			log.info("\t > parsed JWT, header = {}, claims = {}", jws.getHeader(), jws.getBody());
			
			Date exp = jws.getBody().getExpiration();
			if (exp == null) {
				throw new IllegalArgumentException("'exp' is required");
			}
			
			LocalDateTime dateTime = LocalDateTime.ofInstant(exp.toInstant(), ZoneId.systemDefault());
			log.info("\t > exp = {}", dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			
			String username = jws.getBody().getSubject();
			if (!StringUtils.hasText(username)) {
				throw new IllegalArgumentException("'sub' is required");
			}
			
			//return !claims.getExpiration().before(new Date(System.currentTimeMillis()));
			return true;
		} catch (Exception e) { 
			log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
			return false;
			//throw e;
		}
	}
	
	private Jws<Claims> parseJwt(String jwt) {
		return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(jwt);
	}

	private Map<String, Object> createHeader() {
		return Map.of("typ", "JWT", "alg", "HS256");
	}
	
	private Map<String, Object> createClaims(UserDetails userDetails, HttpServletRequest request) {
		UserDetailsDto userDetailsDto = (UserDetailsDto) userDetails;
		Date now = new Date(System.currentTimeMillis());
		Date exp = new Date(now.getTime() + validityInMillis);
		
		Claims claims = Jwts.claims()
				.setSubject(userDetailsDto.getUsername())
				// java.lang.IllegalStateException: No current ServletRequestAttributes
				//.setIssuer(ServletUriComponentsBuilder.fromCurrentContextPath().build().toString())
				.setIssuer(ServletUriComponentsBuilder.fromContextPath(request).build().toString())
				.setIssuedAt(now)
				.setExpiration(exp);
		claims.put("role", userDetailsDto.getMemberRole());
		
		return claims;
	}
	
}
