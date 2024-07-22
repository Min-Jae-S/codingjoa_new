package com.codingjoa.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.codingjoa.propeties.OAuth2Properties;
import com.codingjoa.propeties.OAuth2Properties.KakaoOAuth2Properties;
import com.codingjoa.propeties.OAuth2Properties.NaverOAuth2Properties;
import com.codingjoa.security.dto.KakaoResponseMemberDto;
import com.codingjoa.security.dto.KakaoResponseTokenDto;
import com.codingjoa.security.dto.NaverResponseMemberDto;
import com.codingjoa.security.dto.NaverResponseTokenDto;

@Service
public class OAuth2Service {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private OAuth2Properties oAuth2Properties;
	
	public KakaoResponseTokenDto getKakaoToken(String code) {
		KakaoOAuth2Properties kakaoOAuth2 = oAuth2Properties.getKakaoOAuth2Properties();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", kakaoOAuth2.getAuthorizationGrantType().getValue());
		body.add("client_id", kakaoOAuth2.getClientId());
		body.add("client_secret", kakaoOAuth2.getClientSecret());
		body.add("redirect_uri", kakaoOAuth2.getRedirectUri());
		body.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
		
		ResponseEntity<KakaoResponseTokenDto> response = restTemplate.exchange(
				kakaoOAuth2.getTokenUri(),
				HttpMethod.POST,
				request, 
				KakaoResponseTokenDto.class
		);
		
		return response.getBody();
	}
	
	public KakaoResponseMemberDto getKakaoMember(String accessToken) {
		KakaoOAuth2Properties kakaoOAuth2 = oAuth2Properties.getKakaoOAuth2Properties();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		
		ResponseEntity<KakaoResponseMemberDto> response = restTemplate.exchange(
				kakaoOAuth2.getUserInfoUri(),
				HttpMethod.POST,
				request, 
				KakaoResponseMemberDto.class
		);
		
		return response.getBody();
	}

	public NaverResponseTokenDto getNaverToken(String code, String state) {
		NaverOAuth2Properties naverOAuth2 = oAuth2Properties.getNaverOAuth2Properties();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", naverOAuth2.getAuthorizationGrantType().getValue());
		body.add("client_id", naverOAuth2.getClientId());
		body.add("client_secret", naverOAuth2.getClientSecret());
		body.add("redirect_uri", naverOAuth2.getRedirectUri());
		body.add("code", code);
		body.add("state", state);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
		
		ResponseEntity<NaverResponseTokenDto> response = restTemplate.exchange(
				naverOAuth2.getTokenUri(),
				HttpMethod.POST,
				request, 
				NaverResponseTokenDto.class
		);
		
		return response.getBody();
	}
	
	public NaverResponseMemberDto getNaverMember(String accessToken) {
		NaverOAuth2Properties naverOAuth2 = oAuth2Properties.getNaverOAuth2Properties();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		
		ResponseEntity<NaverResponseMemberDto> response = restTemplate.exchange(
				naverOAuth2.getUserInfoUri(),
				HttpMethod.GET,
				request, 
				NaverResponseMemberDto.class
		);
		
		return response.getBody();
	}
	
}
