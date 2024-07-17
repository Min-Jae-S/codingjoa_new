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
import com.codingjoa.security.dto.KakaoTokenResponseDto;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
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
		
		// authorization code from kakao
		log.info("\t > authorization code from kakao = {}", code);
		
		// access token from kakao
		String accessToken = getKakaoToken(code);
		log.info("\t > accessToken = {}", accessToken);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	private String getKakaoToken(String code) {
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", kakaoClientId);
		body.add("redirect_uri", kakaoRedirectUri);
		body.add("code", code);
		
//		URI uri = UriComponentsBuilder.fromHttpUrl(kakaoAccessTokenUrl)
//			.queryParam("grant_type", "authorization_code")
//			.queryParam("client_id", kakaoClientId)
//			.queryParam("redirect_uri", kakaoRedirectUri)
//			.queryParam("code", code)
//			.build().toUri();

//		Flux<KakaoTokenResponseDto> response = webClient.post()
//				.uri(uri)
//				.contentType(MediaType.APPLICATION_JSON)
//				.retrieve()
//				.bodyToFlux(KakaoTokenResponseDto.class);
		
		log.info("\t > to obtain an access token, send a new request"); 
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<KakaoTokenResponseDto> kakaoTokenResponse = restTemplate.exchange(
				kakaoAccessTokenUrl, 
				HttpMethod.POST, 
				new HttpEntity<>(body, header), 
				KakaoTokenResponseDto.class
		);
		
		KakaoTokenResponseDto kakaoTokenResponseDto = kakaoTokenResponse.getBody();
		log.info("\t > kakaoTokenResponseDto = {}", kakaoTokenResponse.getBody());
		
		return kakaoTokenResponseDto.getAccessToken();
	}
	
	private String getKakaoUserInfo() {
		return null;
	}
	

}
