package com.codingjoa.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.codingjoa.security.dto.NaverResponseMemberDto;
import com.codingjoa.security.dto.NaverResponseTokenDto;
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
		
		KakaoResponseTokenDto kakaoToken = apiService.getKakaoToken(code);
		log.info("\t > 1. obtain kakaoToken ( {} )", kakaoApi.getTokenUrl());
		log.info("\t > kakaoToken = {}", kakaoToken);
		
		KakaoResponseMemberDto kakaoMember = apiService.getKakaoMember(kakaoToken.getAccessToken());
		log.info("\t > 2. obtain kakaoMember ( {} )", kakaoApi.getMemberUrl());
		log.info("\t > kakaoMember = {}", kakaoMember);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/naver/callback")
	public ResponseEntity<Object> naverCallback(@RequestParam String code, @RequestParam String state) throws Exception {
		log.info("## naverCallback");
		log.info("\t > authorization code = {}, state = {}", code, state);
		
		NaverResponseTokenDto naverToken = apiService.getNaverToken(code, state);
		log.info("\t > 1. obtain naverToken ( {} )", naverApi.getTokenUrl());
		log.info("\t > naverToken = {}", naverToken);
		
		NaverResponseMemberDto naverMember = apiService.getNaverMember(naverToken.getAccessToken());
		log.info("\t > 2. obtain naverMember ( {} )", naverApi.getMemberUrl());
		log.info("\t > naverMember = {}", naverMember);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	//	URI uri = UriComponentsBuilder.fromHttpUrl(kakaoAccessTokenUrl)
	//		.queryParam("grant_type", "authorization_code")
	//		.queryParam("client_id", kakaoClientId)
	//		.queryParam("redirect_uri", kakaoRedirectUri)
	//		.queryParam("code", code)
	//		.build().toUri();
	
	//	Flux<KakaoTokenResponseDto> response = webClient.post()
	//		.uri(uri)
	//		.contentType(MediaType.APPLICATION_JSON)
	//		.retrieve()
	//		.bodyToFlux(KakaoTokenResponseDto.class);

}
