package com.codingjoa.security.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Component
public class KakaoApi {
	
	@Value("${security.oauth2.kakao.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.kakao.client-secret}")
	private String clientSecret;
	
	@Value("${security.oauth2.kakao.redirect-uri}")
	private String redirectUri;
	
	@Value("${security.oauth2.kakao.authorize-url}")
	private String authorizeUrl;
	
	@Value("${security.oauth2.kakao.token-url}")
	private String tokenUrl;
	
	@Value("${security.oauth2.kakao.member-url}")
	private String memberUrl;
	
}
