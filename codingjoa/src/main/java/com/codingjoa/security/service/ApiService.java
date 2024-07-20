package com.codingjoa.security.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.codingjoa.security.api.KakaoApi;
import com.codingjoa.security.api.NaverApi;
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
	
	public String getKakaoAccessToken(String code) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", kakaoApi.getClientId());
		//body.add("client_secret", kakaoApi.getClientSecret());
		body.add("redirect_uri", kakaoApi.getRedirectUri());
		body.add("code", code);
		
		RequestEntity<MultiValueMap<String, String>> request = RequestEntity
				.post(new URI(kakaoApi.getTokenUrl()))
				.headers(headers)
				.body(headers);
		
		String jsonKakaoToken = null;
		try {
			ResponseEntity<String> response = restTemplate.exchange(request, String.class);
			jsonKakaoToken = response.getBody();
			log.info("## 1. obtain access token {}", JsonUtils.formatJson(jsonKakaoToken));
		} catch (HttpClientErrorException e) {
			// org.springframework.web.client.HttpClientErrorException$Unauthorized: 401 Unauthorized: [no body]
			log.info("\t > status code = {}", e.getStatusCode());
			log.info("\t > response body = {}", e.getResponseBodyAsString());
			log.info("\t > status text = {}", e.getStatusText());
			log.info("\t > headers = {}", e.getResponseHeaders());
		}
		
		return (String) objectMapper.readValue(jsonKakaoToken, Map.class).get("access_token");
	}
	
	public Map<String, String> getKakaoMember(String accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		RequestEntity<Void> request = RequestEntity
				.post(new URI(kakaoApi.getMemberUrl()))
				.headers(headers)
				.build();
		
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		String jsonKakaoMember= response.getBody();
		log.info("## 2. obtain member {}", JsonUtils.formatJson(jsonKakaoMember));
		
		return objectMapper.readValue(jsonKakaoMember, Map.class);
	}

	public String getNaverAccessToken(String code, String state) throws Exception {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", naverApi.getClientId());
		body.add("client_secret", naverApi.getClientSecret());
		body.add("redirect_uri", naverApi.getRedirectUri());
		body.add("code", code);
		body.add("state", state);
		
		RequestEntity<MultiValueMap<String, String>> request = RequestEntity
				.post(new URI(naverApi.getTokenUrl()))
				.body(body);
		
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		String jsonNaverToken = response.getBody();
		log.info("## 1. obtain access token {}", JsonUtils.formatJson(jsonNaverToken));
		
		return (String) objectMapper.readValue(jsonNaverToken, Map.class).get("access_token");
	}
	
	public Map<String, String> getNaverMember(String accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		RequestEntity<Void> request = RequestEntity
				.get(new URI(naverApi.getMemberUrl()))
				.headers(headers)
				.build();
		
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		String jsonNaverMember= response.getBody();
		log.info("## 2. obtain member {}", JsonUtils.formatJson(jsonNaverMember));
		
		return objectMapper.readValue(jsonNaverMember, Map.class);
	}
	
	public Map<String, String> getNaverAddress(String accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		RequestEntity<Void> request = RequestEntity
				.get(new URI(naverApi.getAddressUrl()))
				.headers(headers)
				.build();
		
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		String jsonNaverAddress= response.getBody();
		log.info("## 3. obtain address {}", JsonUtils.formatJson(jsonNaverAddress));
		
		return objectMapper.readValue(jsonNaverAddress, Map.class);
	}

}
