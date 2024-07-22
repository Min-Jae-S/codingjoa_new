package com.codingjoa.propeties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Component
public class NaverOAuth2 {

	@Value("${security.oauth2.naver.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.naver.client-secret}")
	private String clientSecret;
	
	@Value("${security.oauth2.naver.redirect-url}")
	private String redirectUri;
	
	@Value("${security.oauth2.naver.authorize-url}")
	private String authorizeUrl;
	
	@Value("${security.oauth2.naver.token-url}")
	private String tokenUrl;
	
	@Value("${security.oauth2.naver.member-url}")
	private String memberUrl;
	
}
