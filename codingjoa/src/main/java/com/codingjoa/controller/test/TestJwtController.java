package com.codingjoa.controller.test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.security.dto.PrincipalDetails;
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
	
	private final String secretKey = "JsonWebTokenSecretKeyForJwtVerificationInSpringSecurityAndCodingjoa";
	private final String invalidSecretKey= "InvalidJsonWebTokenSecretKeyForJwtVerificationInSpringSecurityAndCodingjoa";
	private final String username = "smj20228";
	private final long validityInMillis = 1800000; // 1000 * 60 * 30 (30 mins)
	private final Key signingKey; 
	private final JwtProvider jwtProvider;
	private final UserDetailsService userDetailsService;
	
	public TestJwtController(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
		this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.jwtProvider = jwtProvider;
		this.userDetailsService = userDetailsService;
	}

	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		String key = Encoders.BASE64.encode(signingKey.getEncoded()); 
		log.info("\t > key = {}", key);
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/test2")
	public ResponseEntity<Object> test2(HttpServletRequest request) {
		log.info("## test2");

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		String jwt = jwtProvider.createJwt(authentication, request);
		log.info("\t > created JWT = {}", jwt);
		
		Jws<Claims> jws = Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(jwt);
		log.info("\t > JWT header = {}", jws.getHeader().keySet());
		log.info("\t > JWT body = {}", jws.getBody().keySet());
		log.info("\t > JWT signature = {}", jws.getSignature());

		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test3")
	public ResponseEntity<Object> test3() {
		log.info("## test3");
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		Map<String, Object> header = createHeader();
		Map<String, Object> claims = createClaims(userDetails);
		
		// SignatureException 
		log.info("## validate invalid key JWT"); 
		String invalidKeyJwt = Jwts.builder()
				.setHeader(header)
				.setClaims(claims)
				.signWith(createKey(invalidSecretKey), SignatureAlgorithm.HS256)
				.compact();
		log.info("\t > result = {}", jwtProvider.isValidJwt(invalidKeyJwt)); 
		
		log.info("## validate invalid algorithm JWT"); 
		String invalidAlgJwt = Jwts.builder()
				.setHeader(header)
				.setClaims(claims)
				.signWith(signingKey, SignatureAlgorithm.HS512)
				.compact();
		log.info("\t > result = {}", jwtProvider.isValidJwt(invalidAlgJwt));

		// MalformedJwtException
		log.info("## validate malformed JWT"); 
		String malformedJwt = "aaabbbccc"; // "aaa.bbb.ccc"
		log.info("\t > result = {}", jwtProvider.isValidJwt(malformedJwt));

		// IllegalArgumentException 
		log.info("## validate illegal argument JWT"); 
		String illegalArgumentJwt = null; // "", "   "
		log.info("\t > result = {}", jwtProvider.isValidJwt(illegalArgumentJwt));
		
		// SignatureException 
		log.info("## validate multiple invalid JWT"); 
		String multiInvalidJwt = Jwts.builder()
				.setHeader(header)
				.setExpiration(new Date(System.currentTimeMillis() - validityInMillis))
				.signWith(createKey(invalidSecretKey), SignatureAlgorithm.HS256)
				.compact();
		log.info("\t > result = {}", jwtProvider.isValidJwt(multiInvalidJwt));
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/test4")
	public ResponseEntity<Object> test4() {
		log.info("## test4");
		
		// no claims : UnsupportedJwtException 
		log.info("## validate no claims JWT");
		String noClaimsJwt = Jwts.builder().signWith(signingKey, SignatureAlgorithm.HS256).compact();
		log.info("\t > result = {}", jwtProvider.isValidJwt(noClaimsJwt));

		// empty claims : UnsupportedJwtException 
		log.info("## validate empty claims JWT");
		String emptyClaimsJwt = Jwts.builder()
				.setClaims(Collections.emptyMap())
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
		log.info("\t > result = {}", jwtProvider.isValidJwt(emptyClaimsJwt));

		// illegal expiration : IllegalArgumentException
		log.info("## validate illegal expiration JWT");
		String illegalExpJwt = Jwts.builder()
				.setClaims(Map.of("exp", "aaa"))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
		log.info("\t > result = {}", jwtProvider.isValidJwt(illegalExpJwt));
		
		// no expiration : NullPointerException --> IllegalArgumentException
		log.info("## validate no expiration JWT");
		String noExpJwt = Jwts.builder()
				.setClaims(Map.of("email", "smj20228"))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
		log.info("\t > result = {}", jwtProvider.isValidJwt(noExpJwt));
		
		// no expiration : IllegalArgumentException
		log.info("## validate no sub JWT");
		String noSubJwt = Jwts.builder()
				.setClaims(Map.of("exp", new Date(System.currentTimeMillis() + validityInMillis)))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
		log.info("\t > result = {}", jwtProvider.isValidJwt(noSubJwt));
		
		// expired : ExpiredJwtException
		log.info("## validate expired JWT");
		String expiredJwt = Jwts.builder()
				.setClaims(Map.of("exp", new Date(System.currentTimeMillis() - validityInMillis)))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
		log.info("\t > result = {}", jwtProvider.isValidJwt(expiredJwt));
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/test5")
	public ResponseEntity<Object> test5(HttpServletRequest request) {
		log.info("## test5");
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		log.info("\t > header = {}", header == null ? null : "'" + header + "'");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/test6")
	public ResponseEntity<Object> test6(HttpServletRequest request) {
		log.info("## test6");
		String jwt = resolveJwt(request);
		log.info("\t > resolved JWT = {}", jwt == null ? null : "'" + jwt + "'");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/create-jwt")
	public ResponseEntity<Object> createJwt(HttpServletRequest request) {
		log.info("## createJwt");
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		String jwt = jwtProvider.createJwt(authentication, request);
		log.info("\t > created JWT = {}", jwt);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.message("success")
				.data(Map.of("jwt", jwt))
				.build());
	}

	@GetMapping("/send-jwt")
	public ResponseEntity<Object> getJwt(HttpServletRequest request) {
		log.info("## getJwt");
		String jwt = resolveJwt(request);
		log.info("\t > resolved JWT = {}", jwt);
		
		jwtProvider.isValidJwt(jwt);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/check-authentication")
	public ResponseEntity<Object> checkAuthentication() {
		log.info("## checkAuthentication");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			log.info("\t > authentication = {}", auth.getClass().getSimpleName());
			log.info("\t > details = {}", auth.getDetails().getClass().getSimpleName());
		} catch (NullPointerException e) {
			log.info("\t > authentication = null");
		}
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	private Map<String, Object> createHeader() {
		return Map.of("typ", "JWT", "alg", "HS256");
	}
	
	@SuppressWarnings("unused")
	private Map<String, Object> createClaims(UserDetails userDetails) {
		PrincipalDetails principalDetails = (PrincipalDetails) userDetails;
		Date now = new Date(System.currentTimeMillis());
		Date exp = new Date(now.getTime() + validityInMillis);
		
		Claims claims = Jwts.claims()
				//.setSubject(principalDetails.getId())
				.setIssuer(ServletUriComponentsBuilder.fromCurrentContextPath().build().toString())
				.setIssuedAt(now)
				.setExpiration(exp);
		//claims.put("role", principalDetails.getRole());
		
		return claims;
	}
	
	private Key createKey(String str) {
		return Keys.hmacShaKeyFor(str.getBytes(StandardCharsets.UTF_8));
	}
	
	private String resolveJwt(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null && header.startsWith("Bearer ")) {
			return header.split(" ")[1];
		}
		
		return null;
	}
	
	
}
