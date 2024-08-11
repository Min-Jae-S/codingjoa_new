package com.codingjoa.security.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.codingjoa.security.dto.OAuth2UserDto;
import com.codingjoa.security.dto.UserDetailsDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({ "unused", "unchecked" })
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
	private final UserDetailsService userDetailsService;
	
	// since Spring 4.3, if a class has only one constructor, the @Autowired annotation can be omitted.
	@Autowired
	public JwtProvider(@Value("${security.jwt.secret-key}") String secretKey,
						@Value("${security.jwt.validity-in-mills}") long validityInMillis, 
						UserDetailsService userDetailsService) {
		this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.validityInMillis = validityInMillis;
		this.userDetailsService = userDetailsService;
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
		String memberId = claims.getSubject();
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	
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
			List<String> keys = (List<String>) Stream.of(jws.getHeader(), jws.getBody())
				.flatMap(map -> map.keySet().stream())
				.collect(Collectors.toList());
			log.info("\t > parsed JWT = {}", keys);
			
			Date exp = jws.getBody().getExpiration();
			if (exp == null) {
				throw new IllegalArgumentException("'exp' is required");
			}
			
			//LocalDateTime dateTime = LocalDateTime.ofInstant(exp.toInstant(), ZoneId.systemDefault());
			//log.info("\t > exp = {}", dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			
			String email = (String) jws.getBody().get("email");
			if (!StringUtils.hasText(email)) {
				throw new IllegalArgumentException("'email' is required");
			}

			String role = (String) jws.getBody().get("role");
			if (!StringUtils.hasText(role)) {
				throw new IllegalArgumentException("'role' is required");
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
				//.setIssuer(ServletUriComponentsBuilder.fromContextPath(request).build().toUriString())
				.setIssuer(UrlUtils.buildRequestUrl(request))
				.setIssuedAt(now)
				.setExpiration(exp);
		
		// UserDetails(UserDetailsDto), OAuth2User(OAuth2UserDto)
		Object principal = authentication.getPrincipal(); 
		
		if (principal instanceof UserDetailsDto) {
			UserDetailsDto userDetailsDto = (UserDetailsDto) principal;
			claims.setSubject(userDetailsDto.getUsername());
			claims.put("email", userDetailsDto.getMember().getMemberEmail());
			claims.put("role", userDetailsDto.getMemberRole());
			claims.put("image_url", userDetailsDto.getMemberImageUrl());
		} else if (principal instanceof OAuth2UserDto) {
			OAuth2UserDto oAuth2UserDto = (OAuth2UserDto) principal;
			// ...
		}
		
		return claims;
	}
	
}
