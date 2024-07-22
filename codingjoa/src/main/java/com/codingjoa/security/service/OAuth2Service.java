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

import com.codingjoa.security.dto.KakaoUserInfoResponse;
import com.codingjoa.security.dto.KakaoTokenResponse;
import com.codingjoa.security.dto.NaverUserInfoResponse;
import com.codingjoa.security.dto.NaverTokenResponse;
import com.codingjoa.security.oauth2.OAuth2Properties;
import com.codingjoa.security.oauth2.OAuth2Properties.KakaoOAuth2Properties;
import com.codingjoa.security.oauth2.OAuth2Properties.NaverOAuth2Properties;

@Service
public class OAuth2Service {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private OAuth2Properties oAuth2Properties;
	
	public KakaoTokenResponse getKakaoToken(String code) {
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
		
		ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
				kakaoOAuth2.getTokenUri(),
				HttpMethod.POST,
				request, 
				KakaoTokenResponse.class
		);
		
		return response.getBody();
	}
	
	public KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
		KakaoOAuth2Properties kakaoOAuth2 = oAuth2Properties.getKakaoOAuth2Properties();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		
		ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(
				kakaoOAuth2.getUserInfoUri(),
				HttpMethod.POST,
				request, 
				KakaoUserInfoResponse.class
		);
		
		return response.getBody();
	}

	public NaverTokenResponse getNaverToken(String code, String state) {
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
		
		ResponseEntity<NaverTokenResponse> response = restTemplate.exchange(
				naverOAuth2.getTokenUri(),
				HttpMethod.POST,
				request, 
				NaverTokenResponse.class
		);
		
		return response.getBody();
	}
	
	public NaverUserInfoResponse getNaverUserInfo(String accessToken) {
		NaverOAuth2Properties naverOAuth2 = oAuth2Properties.getNaverOAuth2Properties();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		
		ResponseEntity<NaverUserInfoResponse> response = restTemplate.exchange(
				naverOAuth2.getUserInfoUri(),
				HttpMethod.GET,
				request, 
				NaverUserInfoResponse.class
		);
		
		return response.getBody();
	}
	
}
