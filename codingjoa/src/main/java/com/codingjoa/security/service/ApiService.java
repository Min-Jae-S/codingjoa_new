package com.codingjoa.security.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.codingjoa.security.api.KakaoApi;
import com.codingjoa.security.api.NaverApi;
import com.codingjoa.security.dto.KakaoResponseMemberDto;
import com.codingjoa.security.dto.KakaoResponseTokenDto;
import com.codingjoa.security.dto.NaverResponseTokenDto;
import com.codingjoa.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({ "unused", "unchecked" })
@Slf4j
@Service
public class ApiService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();
	
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
		log.info("## obtain access token {}", JsonUtils.formatJson(response.getBody()));
		
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
		log.info("## obtain kakao member {}", JsonUtils.formatJson(response.getBody()));
		
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
		log.info("## obtain access token {}", JsonUtils.formatJson(response.getBody()));
		
		return response.getBody().getAccessToken();
	}
	
	public Map<String, String> getNaverMember(String accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				naverApi.getMemberUrl(),
				HttpMethod.GET,
				request, 
				String.class
		);
		
		String jsonNaverMember= response.getBody();
		log.info("## obtain naver member {}", JsonUtils.formatJson(jsonNaverMember));
		
		return objectMapper.readValue(jsonNaverMember, Map.class);
	}
	
	public Map<String, String> getNaverAddress(String accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				naverApi.getAddressUrl(),
				HttpMethod.GET,
				request, 
				String.class
		);
		
		String jsonNaverAddress= response.getBody();
		log.info("## obtain naver address {}", JsonUtils.formatJson(jsonNaverAddress));
		
		return objectMapper.readValue(jsonNaverAddress, Map.class);
	}

}
