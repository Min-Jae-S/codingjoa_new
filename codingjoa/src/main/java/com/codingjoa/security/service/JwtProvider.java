package com.codingjoa.security.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.util.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	// since Spring 4.3, if a class has only one constructor, the @Autowired annotation can be omitted.
	@Autowired
	public JwtProvider(@Value("${security.jwt.secret-key}") String secretKey,
						@Value("${security.jwt.validity-in-mills}") long validityInMillis) { 
		this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.validityInMillis = validityInMillis;
	}
	
	/*
	 * header - typ, alg
	 * claims - sub, iss, iat, exp, email, role
	 */
	public String createJwt(Authentication authentication, HttpServletRequest request) {
		return Jwts.builder()
				.setHeader(createHeader()) 
				.setClaims(createClaims(authentication, request))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
	}

	// https://velog.io/@tmdgh0221/Spring-Security-%EC%99%80-OAuth-2.0-%EC%99%80-JWT-%EC%9D%98-%EC%BD%9C%EB%9D%BC%EB%B3%B4
	// check comment: to fully leverage the advantages of using JWT, it's preferable to avoid database access during the verification process.
	public Authentication getAuthentication(String jwt) {
		Claims claims = parseJwt(jwt).getBody();
		PrincipalDetails pincipalDetails = PrincipalDetails.from(claims);
		log.info("{}", Utils.formatPrettyJson(pincipalDetails));
		
		return new UsernamePasswordAuthenticationToken(pincipalDetails, null, pincipalDetails.getAuthorities());
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
			Claims claims = jws.getBody();
			log.info("\t > parsed JWT = {}", claims);
			
			String idx = claims.getSubject();
			if (!StringUtils.hasText(idx)) {
				throw new IllegalArgumentException("'idx' is required");
			}
			
			String email = (String) claims.get("email");
			if (!StringUtils.hasText(email)) {
				throw new IllegalArgumentException("'email' is required");
			}

			String nickname = (String) claims.get("nickname");
			if (!StringUtils.hasText(nickname)) {
				throw new IllegalArgumentException("'nickname' is required");
			}

			String role = (String) claims.get("role");
			if (!StringUtils.hasText(role)) {
				throw new IllegalArgumentException("'role' is required");
			}
			
			String provider = (String) claims.get("provider");
			if (!StringUtils.hasText(provider)) {
				throw new IllegalArgumentException("'provider' is required");
			}
			
			if (claims.getExpiration() == null) {
				throw new IllegalArgumentException("'exp' is required");
			}
			
			return true;
			//return !claims.getExpiration().before(new Date(System.currentTimeMillis()));
		} catch (Exception e) { 
			log.info("\t > missing or invalid JWT : {}", e.getMessage());
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
	
	private Map<String, Object> createClaims(Authentication authentication, HttpServletRequest request) {
		Date now = new Date(System.currentTimeMillis());
		Date exp = new Date(now.getTime() + validityInMillis);
		
		Claims claims = Jwts.claims()
				// java.lang.IllegalStateException: No current ServletRequestAttributes
				//.setIssuer(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString())
				.setIssuer(ServletUriComponentsBuilder.fromContextPath(request).build().toUriString())
				.setIssuedAt(now)
				.setExpiration(exp);
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		claims.setSubject(String.valueOf(principal.getIdx()));
		claims.put("email", principal.getEmail());
		claims.put("nickname", principal.getNickname());
		claims.put("role", principal.getRole());
		claims.put("image_url", principal.getImageUrl());

		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			claims.put("provider", "local");
		} else if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
			claims.put("provider", oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
		}
		
		log.info("\t > created claims = {}", Utils.formatPrettyJson(claims));
		return claims;
	}
	
}
