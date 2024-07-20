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

import com.codingjoa.security.api.KakaoApi;
import com.codingjoa.security.api.NaverApi;
import com.codingjoa.security.dto.KakaoResponseMemberDto;
import com.codingjoa.security.dto.KakaoResponseTokenDto;
import com.codingjoa.security.dto.NaverResponseMemberDto;
import com.codingjoa.security.dto.NaverResponseTokenDto;
import com.codingjoa.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApiService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private KakaoApi kakaoApi;
	
	@Autowired
	private NaverApi naverApi;
	
	public String getKakaoAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", kakaoApi.getClientId());
		body.add("client_secret", kakaoApi.getClientSecret());
		body.add("redirect_uri", kakaoApi.getRedirectUri());
		body.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
		
		ResponseEntity<KakaoResponseTokenDto> response = restTemplate.exchange(
				kakaoApi.getTokenUrl(),
				HttpMethod.POST,
				request, 
				KakaoResponseTokenDto.class
		);
		log.info("## obtain accessToken {}", JsonUtils.formatJson(response.getBody()));
		
		return response.getBody().getAccessToken();
	}
	
	public KakaoResponseMemberDto getKakaoMember(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		
		ResponseEntity<KakaoResponseMemberDto> response = restTemplate.exchange(
				kakaoApi.getMemberUrl(),
				HttpMethod.POST,
				request, 
				KakaoResponseMemberDto.class
		);
		log.info("## obtain kakaoMember {}", JsonUtils.formatJson(response.getBody()));
		
		return response.getBody();
	}

	public String getNaverAccessToken(String code, String state) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", naverApi.getClientId());
		body.add("client_secret", naverApi.getClientSecret());
		body.add("redirect_uri", naverApi.getRedirectUri());
		body.add("code", code);
		body.add("state", state);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
		
		ResponseEntity<NaverResponseTokenDto> response = restTemplate.exchange(
				naverApi.getTokenUrl(),
				HttpMethod.POST,
				request, 
				NaverResponseTokenDto.class
		);
		log.info("## obtain accessToken {}", JsonUtils.formatJson(response.getBody()));
		
		return response.getBody().getAccessToken();
	}
	
	public NaverResponseMemberDto getNaverMember(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		
		ResponseEntity<NaverResponseMemberDto> response = restTemplate.exchange(
				naverApi.getMemberUrl(),
				HttpMethod.GET,
				request, 
				NaverResponseMemberDto.class
		);
		log.info("## obtain naverMember {}", JsonUtils.formatJson(response.getBody()));
		
		return response.getBody();
	}
	
}
