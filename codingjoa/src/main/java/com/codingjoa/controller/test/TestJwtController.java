package com.codingjoa.controller.test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.entity.Member;
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.security.service.JwtProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/jwt")
@RestController
public class TestJwtController {

	/*
	 * @ https://yuma1029.tistory.com/15
	 * @ JWT (Json Web Token)
	 * 		- xxxxx.yyyyy.zzzzz 	// header.payload.signature
	 * 		- header 				// type(typ), algorithm(alg)
	 * 		- payload				// issuer(iss), subject(sub), audience(aud), issued at(iat), expired(exp) - claims
	 * 		- signature
	 */
	
	private final String SECRET_KEY = "JsonWebTokenSecretKeyForJwtAuthenticationInSpringSecurity";
	private final long VALIDITY_IN_MILLIS = 1800000;
	private final Key signingKey; 
	private final JwtProvider jwtProvider;
	private final UserDetailsService userDetailsService;
	
	public TestJwtController(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
		this.signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		this.jwtProvider = jwtProvider;
		this.userDetailsService = userDetailsService;
	}

	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		String key = Encoders.BASE64.encode(signingKey.getEncoded()); 
		log.info("\t > key = {}", key); // SnNvbldlYlRva2VuU2VjcmV0S2V5Rm9ySnd0QXV0aGVudGljYXRpb25JblNwcmluZ1NlY3VyaXR5
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");

		UserDetails userDetails = userDetailsService.loadUserByUsername("smj20228");
		String token = jwtProvider.createToken(userDetails);
		log.info("\t > JWT = {}", token);
		
		Jws<Claims> jws = Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token);
		log.info("\t > JWT header = {}", jws.getHeader().keySet());
		log.info("\t > JWT body = {}", jws.getBody().keySet());
		log.info("\t > JWT signature = {}", jws.getSignature());

		return ResponseEntity.ok(SuccessResponse.builder()
				.message("success")
				.data(Map.of("token", token))
				.build());
	}

	@GetMapping("/invalid-key")
	public ResponseEntity<Object> invalidKeyTest() {
		log.info("## invalidKeyTest");
		
		UserDetails userDetails = userDetailsService.loadUserByUsername("smj20228");
		Map<String, Object> header = createHeader();
		Map<String, Object> claims = createClaims(userDetails);
		Key invalidKey = createKey("JsonWebTokenAuthenticationWithSpringBootTestProjectSecretKey");
		
		String invalidKeyToken = Jwts.builder()
				.setHeader(header)
				.setClaims(claims)
				.signWith(invalidKey, SignatureAlgorithm.HS256)
				.compact();

		String invalidAlgToken = Jwts.builder()
				.setHeader(header)
				.setClaims(claims)
				.signWith(signingKey, SignatureAlgorithm.HS512)
				.compact();
		
		log.info("\t > invalidKeyToken result = {}", jwtProvider.validateToken(invalidKeyToken));
		log.info("\t > invalidAlgToken result = {}", jwtProvider.validateToken(invalidAlgToken));
				
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
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
	
	private Key createKey(String str) {
		return Keys.hmacShaKeyFor(str.getBytes(StandardCharsets.UTF_8));
	}
	
}
