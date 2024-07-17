package com.codingjoa.controller.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.codingjoa.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/oauth2")
@RestController
public class TestOAuth2Controller {
	
	@Value("${security.oauth2.kakao.client-id}")
	private String kakaoClientId;

	@Value("${security.oauth2.kakao.redirect-uri}")
	private String kakaoRedirectUri;
	
	@Value("${security.oauth2.kakao.access-token-url}")
	private String kakaoAccessTokenUrl;
	
	@GetMapping("/kakao/callback")
	public ResponseEntity<Object> kakaoCallback(@RequestParam String code) {
		log.info("## kakaoCallback");
		
		// get authorization code from kakao
		log.info("\t > authorization code = {}", code);
		
		// header : conetent-type
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		// body : grant_type, client_id, redirect_uri, code
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", kakaoClientId);
		body.add("redirect_uri", kakaoRedirectUri);
		body.add("code", code);
		
//		URI uri = UriComponentsBuilder.fromHttpUrl(kakaoAccessTokenUrl)
//				.queryParam("grant_type", "authorization_code")
//				.queryParam("client_id", kakaoClientId)
//				.queryParam("redirect_uri", kakaoRedirectUri)
//				.queryParam("code", code)
//				.build().toUri();
		
		// send a new request to receive access token
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Object> response = restTemplate.exchange(
				kakaoAccessTokenUrl, 
				HttpMethod.POST, 
				new HttpEntity<>(body, header), 
				Object.class
		);
		log.info("\t > response = {}", response);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

}
