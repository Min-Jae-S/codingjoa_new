package com.codingjoa.security.oauth2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class OAuth2ClientProperties {
	
	private final Map<String, Registration> registration = new HashMap<>();
	private final Map<String, Provider> provider = new HashMap<>();
	
	@PostConstruct
	public void init(
			@Value("${security.oauth2.client.registration.kakao.client-id}") String kakaoClientId,
            @Value("${security.oauth2.client.registration.kakao.client-secret}") String kakaoClientSecret,
            @Value("${security.oauth2.client.registration.kakao.client-name}") String kakaoClientName,
            @Value("${security.oauth2.client.registration.kakao.redirect-uri}") String kakaoRedirectUri,
            @Value("${security.oauth2.client.registration.kakao.authorization-grant-type}") String kakaoAuthorizationGrantType,
            @Value("${security.oauth2.client.registration.kakao.scope}") String kakaoScope,
            @Value("${security.oauth2.client.provider.kakao.authorization-uri}") String kakaoAuthorizationUri,
            @Value("${security.oauth2.client.provider.kakao.token-uri}") String kakaoTokenUri,
            @Value("${security.oauth2.client.provider.kakao.user-info-uri}") String kakaoUserInfoUri,
            @Value("${security.oauth2.client.registration.kakao.user-name-attribute}") String kakaoUserNameAttribute,
            @Value("${security.oauth2.client.registration.naver.client-id}") String naverClientId,
            @Value("${security.oauth2.client.registration.naver.client-secret}") String naverClientSecret,
            @Value("${security.oauth2.client.registration.naver.client-name}") String naverClientName,
            @Value("${security.oauth2.client.registration.naver.redirect-uri}") String naverRedirectUri,
            @Value("${security.oauth2.client.registration.naver.authorization-grant-type}") String naverAuthorizationGrantType,
            @Value("${security.oauth2.client.registration.naver.scope}") String naverScope,
            @Value("${security.oauth2.client.provider.naver.authorization-uri}") String naverAuthorizationUri,
            @Value("${security.oauth2.client.provider.naver.token-uri}") String naverTokenUri,
            @Value("${security.oauth2.client.provider.naver.user-info-uri}") String naverUserInfoUri,
            @Value("${security.oauth2.client.registration.naver.user-name-attribute}") String naverUserNameAttribute) {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > kakaoClientId = {}", kakaoClientId);
	}
	
	@Getter
	@Setter
	public static class Registration {
		private String clientId;
		private String clientSecret;
		private String redirectUri;
		private String authorizationGrantType;
		private Set<String> scope;
		private String userNameAttribute;
	}
	
	@Getter
	@Setter
	public static class Provider {
		private String authorizationUri;
		private String tokenUri;
		private String userInfoUri;
	}
	
}
