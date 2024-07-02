package com.codingjoa.controller.test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.response.SuccessResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@PropertySource("/WEB-INF/properties/security.properties")
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
	
	private final String SECRET_KEY;
	private final long VALIDITY_IN_MILLIS;
	private final Key KEY;
	private final UserDetailsService userDetailsService;
	
	public TestJwtController(@Value("${security.jwt.secret-key}") String secretKey, 
			@Value("${security.jwt.validity-in-mills}") long validityInMillis, UserDetailsService userDetailsService) {
		this.SECRET_KEY = secretKey;
		this.VALIDITY_IN_MILLIS = validityInMillis;
		this.KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		//this.KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.userDetailsService = userDetailsService;
	}
	
	@GetMapping("/create-key")
	public ResponseEntity<Object> createkey() {
		log.info("## createkey");
		
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		String base64Key = Encoders.BASE64.encode(key.getEncoded());
		log.info("\t > key = {}", base64Key);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/compare-key")
	public ResponseEntity<Object> comparekey() {
		log.info("## comparekey");
		log.info("\t > KEY = {}", KEY); // SnNvbldlYlRva2VuU2VjcmV0S2V5Rm9ySnd0QXV0aGVudGljYXRpb25JblNwcmluZ1NlY3VyaXR5
		log.info("\t > key1 = {}", Encoders.BASE64.encode(KEY.getEncoded()));

		Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		String key2 = Encoders.BASE64.encode(key.getEncoded());
		log.info("\t > key2 = {}", key2);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/create-token")
	public ResponseEntity<Object> createTokean(HttpServletRequest request) {
		log.info("## createToken");
		
		Date now = new Date(System.currentTimeMillis());
		Date exp = new Date(now.getTime() + Duration.ofMinutes(30).toMillis());
		Date exp2 = new Date(now.getTime() + VALIDITY_IN_MILLIS);
		log.info("\t > now = {}", now.getTime());
		log.info("\t > exp = {}", exp.getTime());
		log.info("\t > exp2 = {}", exp2.getTime());
		
		// issuer(iss), subject(sub), audience(aud), issued at(iat), expired(exp) [claim]
		Claims claims = Jwts.claims();
		String issuer = ServletUriComponentsBuilder.fromCurrentContextPath()
				.build()
				.toString();
		claims.setIssuer(issuer);
		claims.setIssuedAt(now);
		claims.setExpiration(exp);
		log.info("\t > claims = {}", claims);
		
		String token = Jwts.builder()
			.setClaims(claims)
			.signWith(KEY, SignatureAlgorithm.HS256)
			.compact();
		log.info("\t > token1 = {}", token);
		
		String token2 = Jwts.builder()
				.setIssuer(issuer)
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(KEY, SignatureAlgorithm.HS256)
				.compact();
		log.info("\t > token2 = {}", token2);
		log.info("\t > token1 == token2 ? {}", token.equals(token2));
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.message("success")
				.data(Map.of("token", token))
				.build());
	}
	
}
