package com.codingjoa.controller.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.codingjoa.security.api.KakaoApi;
import com.codingjoa.security.api.NaverApi;
import com.codingjoa.security.dto.KakaoResponseMemberDto;
import com.codingjoa.security.dto.KakaoResponseTokenDto;
import com.codingjoa.security.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequestMapping("/test/oauth2")
@RestController
public class TestOAuth2Controller {
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private KakaoApi kakaoApi;
	
	@Autowired
	private NaverApi naverApi;
	
	@Autowired
	private ApiService apiService;
	
	@GetMapping("/kakao/callback")
	public ResponseEntity<Object> kakaoCallback(@RequestParam String code) throws Exception {
		log.info("## kakaoCallback");
		log.info("\t > authorization code = {}", code);
		
		// 1. obtain access token ( https://kauth.kakao.com/oauth/token )
		String accessToken = apiService.getKakaoAccessToken(code);
		
		// 2. obtain member ( https://kapi.kakao.com/v2/user/me )
		KakaoResponseMemberDto kakaoMember = apiService.getKakaoMember(accessToken);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/naver/callback")
	public ResponseEntity<Object> naverCallback(@RequestParam String code, @RequestParam String state) throws Exception {
		log.info("## naverCallback");
		log.info("\t > authorization code = {}", code);
		log.info("\t > state = {}", state);
		
		// 1. obtain access token ( https://nid.naver.com/oauth2.0/token )
		String accessToken = apiService.getNaverAccessToken(code, state);
		
		// 2. obtain member ( https://openapi.naver.com/v1/nid/me )
		Map<String, String> naverMember = apiService.getNaverMember(accessToken);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	private String getKakaoToken(String code) throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", kakaoApi.getClientId());
		body.add("client_secret", kakaoApi.getClientSecret());
		body.add("redirect_uri", kakaoApi.getRedirectUri());
		body.add("code", code);
		
		RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
				.post(new URI(kakaoApi.getTokenUrl()))
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
		
		ResponseEntity<KakaoResponseTokenDto> responseEntity = restTemplate.exchange(requestEntity, KakaoResponseTokenDto.class);
		
		return responseEntity.getBody().getAccessToken();
	}
	
	private KakaoResponseMemberDto getKakaoMember(String accessToken) throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		RequestEntity<Void> requestEntity = RequestEntity
				.post(new URI(kakaoApi.getMemberUrl()))
				.headers(headers)
				.body(null);
		ResponseEntity<KakaoResponseMemberDto> responseEntity = restTemplate.exchange(requestEntity, KakaoResponseMemberDto.class);
		
		return responseEntity.getBody();
	}
	
	private String getNaverToken(String code) throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", naverApi.getClientId());
		body.add("redirect_uri", naverApi.getRedirectUri());
		body.add("code", code);
		
		RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
				.post(new URI(naverApi.getTokenUrl()))
				.body(body);
		
		ResponseEntity<KakaoResponseTokenDto> responseEntity = restTemplate.exchange(requestEntity, KakaoResponseTokenDto.class);
		log.info("\t > naverTokenResponseDto = {}", responseEntity.getBody());
		
		return responseEntity.getBody().getAccessToken();
	}
	

}
