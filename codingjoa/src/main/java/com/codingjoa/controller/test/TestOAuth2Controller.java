package com.codingjoa.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.KakaoUserInfoResponse;
import com.codingjoa.security.dto.KakaoTokenResponse;
import com.codingjoa.security.dto.NaverUserInfoResponse;
import com.codingjoa.security.dto.NaverTokenResponse;
import com.codingjoa.security.service.OAuth2Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/oauth2")
@RestController
public class TestOAuth2Controller {
	
	@Autowired
	private OAuth2Service oAuth2Service;
	
	@GetMapping("/kakao/callback")
	public ResponseEntity<Object> kakaoCallback(@RequestParam String code) throws Exception {
		log.info("## kakaoCallback");
		log.info("\t > authorization code = {}", code);
		
		KakaoTokenResponse kakaoToken = oAuth2Service.getKakaoToken(code);
		log.info("\t > 1. request kakaoToken ( https://kauth.kakao.com/oauth/token )");
		log.info("\t > {}", kakaoToken);
		
		KakaoUserInfoResponse kakaoUserInfo = oAuth2Service.getKakaoUserInfo(kakaoToken.getAccessToken());
		log.info("\t > 2. request kakaoUserInfo ( https://kapi.kakao.com/v2/user/me )");
		log.info("\t > {}", kakaoUserInfo);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/naver/callback")
	public ResponseEntity<Object> naverCallback(@RequestParam String code, @RequestParam String state) throws Exception {
		log.info("## naverCallback");
		log.info("\t > authorization code = {}, state = {}", code, state);
		
		NaverTokenResponse naverToken = oAuth2Service.getNaverToken(code, state);
		log.info("\t > 1. request naverToken ( https://nid.naver.com/oauth2.0/token )");
		log.info("\t > {}", naverToken);
		
		NaverUserInfoResponse naverUserInfo = oAuth2Service.getNaverUserInfo(naverToken.getAccessToken());
		log.info("\t > 2. request naverUserInfo ( https://openapi.naver.com/v1/nid/me )");
		log.info("\t > {}", naverUserInfo);
		
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
