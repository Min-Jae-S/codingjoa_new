package com.codingjoa.controller.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.KakaoMemberDto;
import com.codingjoa.security.dto.KakaoTokenResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Value("${security.oauth2.kakao.token-url}")
	private String kakaoTokenUrl;

	@Value("${security.oauth2.kakao.member-url}")
	private String kakaoMemberUrl;
	
	@GetMapping("/kakao/callback")
	public ResponseEntity<Object> kakaoCallback(@RequestParam String code) throws URISyntaxException {
		log.info("## kakaoCallback");
		
		// authorization code from kakao
		log.info("\t > authorization code = {}", code);
		
		String accessToken = getKakaoToken(code);
		log.info("\t > accessToken = {}", accessToken);
		
//		KakaoMemberDto kakaoMemberDto = getKakaoMember(accessToken);
//		log.info("\t > kakaoMemberDto = {}", kakaoMemberDto);
		
		String jsonKaKaoMember = getKakaoMember(accessToken);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Object json = objectMapper.readValue(jsonKaKaoMember, Object.class);
			String prettyStirng = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			log.info("\t > jsonKaKaoMember = {}{}", System.lineSeparator(), prettyStirng);
		} catch (JsonProcessingException e) {
			log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	private String getKakaoToken(String code) throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", kakaoClientId);
		body.add("redirect_uri", kakaoRedirectUri);
		body.add("code", code);
		
		RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
				.post(new URI(kakaoTokenUrl))
				.headers(headers)
				.body(body);
		
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
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<KakaoTokenResponseDto> responseEntity = restTemplate.exchange(requestEntity, KakaoTokenResponseDto.class);
		log.info("\t > KakaoTokenResponseDto = {}", responseEntity.getBody());
		
		return responseEntity.getBody().getAccessToken();
	}
	
	private String getKakaoMember(String accessToken) throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		RequestEntity<Void> requestEntity = RequestEntity
				.post(new URI(kakaoMemberUrl))
				.headers(headers)
				.body(null);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		
		return responseEntity.getBody();
	}
	
//	private KakaoMemberDto getKakaoMember(String accessToken) throws URISyntaxException {
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
//		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
//		
//		RequestEntity<Void> requestEntity = RequestEntity
//				.post(new URI(kakaoMemberUrl))
//				.headers(headers)
//				.body(null);
//		
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<KakaoMemberDto> responseEntity = restTemplate.exchange(requestEntity, KakaoMemberDto.class);
//		
//		return responseEntity.getBody();
//	}

}
