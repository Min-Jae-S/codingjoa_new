package com.codingjoa.security.oauth2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.codingjoa.security.dto.KakaoTokenResponse;
import com.codingjoa.security.dto.KakaoUserInfoResponse;
import com.codingjoa.security.dto.NaverTokenResponse;
import com.codingjoa.security.dto.NaverUserInfoResponse;
import com.codingjoa.util.FormatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestOAuth2Service {
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ClientRegistration kakaoRegistration;
	private final ClientRegistration naverRegistration;
	
	@Autowired
	public TestOAuth2Service(@Qualifier("testClientRegistrationRepository") ClientRegistrationRepository clientRegistrationRepository) {
		this.kakaoRegistration = clientRegistrationRepository.findByRegistrationId("kakao");
		this.naverRegistration = clientRegistrationRepository.findByRegistrationId("naver");
	}
	
	public KakaoTokenResponse getKakaoToken(String code, String state) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", kakaoRegistration.getAuthorizationGrantType().getValue());
		body.add("client_id", kakaoRegistration.getClientId());
		body.add("client_secret", kakaoRegistration.getClientSecret());
		body.add("redirect_uri", kakaoRegistration.getRedirectUriTemplate());
		body.add("code", code);
		body.add("state", state);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				kakaoRegistration.getProviderDetails().getTokenUri(),
				HttpMethod.POST,
				request, 
				String.class
		);
		
		String responseBody = response.getBody();
		log.info("{}", FormatUtils.formatPrettyJson(responseBody));
		
		return parseJson(responseBody, KakaoTokenResponse.class);
	}
	
	public KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				kakaoRegistration.getProviderDetails().getUserInfoEndpoint().getUri(),
				HttpMethod.POST,
				request, 
				String.class
		);
		
		String responseBody = response.getBody();
		log.info("{}", FormatUtils.formatPrettyJson(responseBody));
		
		return parseJson(responseBody, KakaoUserInfoResponse.class);
	}

	public NaverTokenResponse getNaverToken(String code, String state) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", naverRegistration.getAuthorizationGrantType().getValue());
		body.add("client_id", naverRegistration.getClientId());
		body.add("client_secret", naverRegistration.getClientSecret());
		body.add("redirect_uri", naverRegistration.getRedirectUriTemplate());
		body.add("code", code);
		body.add("state", state);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				naverRegistration.getProviderDetails().getTokenUri(),
				HttpMethod.POST,
				request, 
				String.class
		);
		
		String responseBody = response.getBody();
		log.info("{}", FormatUtils.formatPrettyJson(responseBody));
		
		return parseJson(responseBody, NaverTokenResponse.class);
	}
	
	public NaverUserInfoResponse getNaverUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				naverRegistration.getProviderDetails().getUserInfoEndpoint().getUri(),
				HttpMethod.GET,
				request, 
				String.class
		);
		
		String responseBody = response.getBody();
		log.info("{}", FormatUtils.formatPrettyJson(responseBody));
		
		return parseJson(responseBody, NaverUserInfoResponse.class);
	}
	
	private <T> T parseJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
			return null;
		}
	}
	
}
