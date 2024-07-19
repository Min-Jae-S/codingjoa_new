package com.codingjoa.security.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class NaverApi {

	@Value("${security.oauth2.naver.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.naver.redirect-uri}")
	private String redirectUri;
	
	@Value("${security.oauth2.naver.authorization.url}")
	private String authorizationUrl;
	
	@Value("${security.oauth2.naver.token.url}")
	private String tokenUrl;
	
	@Value("${security.oauth2.naver.member.url}")
	private String memberUrl;
	
}
