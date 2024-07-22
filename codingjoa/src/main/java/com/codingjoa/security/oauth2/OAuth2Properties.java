package com.codingjoa.security.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Component
public class OAuth2Properties {
	
	public OAuth2Properties(
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
            @Value("${security.oauth2.client.registration.naver.user-name-attribute}") String naverUserNameAttribute
    ) {
		this.kakaoOAuth2Properties = new KakaoOAuth2Properties(kakaoClientId, kakaoClientSecret, kakaoClientName,
				kakaoRedirectUri, new AuthorizationGrantType(kakaoAuthorizationGrantType), kakaoScope,
				kakaoAuthorizationUri, kakaoTokenUri, kakaoUserInfoUri, kakaoUserNameAttribute);
		this.naverOAuth2Properties = new NaverOAuth2Properties(naverClientId, naverClientSecret, naverClientName,
				naverRedirectUri, new AuthorizationGrantType(naverAuthorizationGrantType), naverScope,
				naverAuthorizationUri, naverTokenUri, naverUserInfoUri, naverUserNameAttribute);
    }
	
	private final KakaoOAuth2Properties kakaoOAuth2Properties;
	private final NaverOAuth2Properties naverOAuth2Properties;
	
	@ToString
	@Getter
	@RequiredArgsConstructor
	public static class KakaoOAuth2Properties {
		
		private final String clientId;
		private final String clientSecret;
		private final String clinetName;
		private final String redirectUri;
		private final AuthorizationGrantType authorizationGrantType;
		private final String scope;
		private final String authorizationUri;
		private final String tokenUri;
		private final String userInfoUri;
		private final String userNameAttribute;
	}

	@ToString
	@Getter
	@RequiredArgsConstructor
	public static class NaverOAuth2Properties {
		
		private final String clientId;
		private final String clientSecret;
		private final String clinetName;
		private final String redirectUri;
		private final AuthorizationGrantType authorizationGrantType;
		private final String scope;
		private final String authorizationUri;
		private final String tokenUri;
		private final String userInfoUri;
		private final String userNameAttribute;
		
	}
}
