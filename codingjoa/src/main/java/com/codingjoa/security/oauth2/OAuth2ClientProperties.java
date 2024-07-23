package com.codingjoa.security.oauth2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Component
//@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class OAuth2ClientProperties {
	
	private final Map<String, Registration> registration = new HashMap<>();
	private final Map<String, Provider> provider = new HashMap<>();
	
	public OAuth2ClientProperties(
			@Value("${security.oauth2.client.registration.kakao.client-id}") String kakaoClientId,
            @Value("${security.oauth2.client.registration.kakao.client-secret}") String kakaoClientSecret,
            @Value("${security.oauth2.client.registration.kakao.redirect-uri}") String kakaoRedirectUri,
            @Value("${security.oauth2.client.registration.kakao.authorization-grant-type}") String kakaoAuthorizationGrantType,
            @Value("${security.oauth2.client.registration.kakao.scope}") List<String> kakaoScope,
            @Value("${security.oauth2.client.provider.kakao.authorization-uri}") String kakaoAuthorizationUri,
            @Value("${security.oauth2.client.provider.kakao.token-uri}") String kakaoTokenUri,
            @Value("${security.oauth2.client.provider.kakao.user-info-uri}") String kakaoUserInfoUri,
            @Value("${security.oauth2.client.registration.kakao.user-name-attribute}") String kakaoUserNameAttribute,
            @Value("${security.oauth2.client.registration.naver.client-id}") String naverClientId,
            @Value("${security.oauth2.client.registration.naver.client-secret}") String naverClientSecret,
            @Value("${security.oauth2.client.registration.naver.redirect-uri}") String naverRedirectUri,
            @Value("${security.oauth2.client.registration.naver.authorization-grant-type}") String naverAuthorizationGrantType,
            @Value("${security.oauth2.client.registration.naver.scope}") List<String> naverScope,
            @Value("${security.oauth2.client.provider.naver.authorization-uri}") String naverAuthorizationUri,
            @Value("${security.oauth2.client.provider.naver.token-uri}") String naverTokenUri,
            @Value("${security.oauth2.client.provider.naver.user-info-uri}") String naverUserInfoUri,
            @Value("${security.oauth2.client.registration.naver.user-name-attribute}") String naverUserNameAttribute) {
		Registration kakaoRegistration = new Registration();
		kakaoRegistration.setClientId(kakaoClientId);
		kakaoRegistration.setClientSecret(kakaoClientSecret);
		kakaoRegistration.setRedirectUri(kakaoRedirectUri);
		kakaoRegistration.setAuthorizationGrantType(kakaoAuthorizationGrantType);
		kakaoRegistration.setScope(new HashSet<String>(kakaoScope));
		kakaoRegistration.setUserNameAttribute(kakaoUserNameAttribute);
		registration.put("kakao", kakaoRegistration);
		
		Provider kakaoProvider = new Provider();
		kakaoProvider.setAuthorizationUri(kakaoAuthorizationUri);
		kakaoProvider.setTokenUri(kakaoTokenUri);
		kakaoProvider.setUserInfoUri(kakaoUserInfoUri);
		provider.put("kakao", kakaoProvider);
		
		Registration naverRegistration = new Registration();
		naverRegistration.setClientId(naverClientId);
		naverRegistration.setClientSecret(naverClientSecret);
		naverRegistration.setRedirectUri(naverRedirectUri);
		naverRegistration.setAuthorizationGrantType(naverAuthorizationGrantType);
		naverRegistration.setScope(new HashSet<String>(naverScope));
		naverRegistration.setUserNameAttribute(naverUserNameAttribute);
		registration.put("naver", naverRegistration);
		
		Provider naverProvider = new Provider();
		naverProvider.setAuthorizationUri(naverAuthorizationUri);
		naverProvider.setTokenUri(naverTokenUri);
		naverProvider.setUserInfoUri(naverUserInfoUri);
		provider.put("naver", naverProvider);
	}
	
	@Setter
	@Getter
	public static class Registration {
		private String clientId;
		private String clientSecret;
		private String redirectUri;
		private String authorizationGrantType;
		private Set<String> scope;
		private String userNameAttribute;
	}
	
	@Setter
	@Getter
	public static class Provider {
		private String authorizationUri;
		private String tokenUri;
		private String userInfoUri;
	}
	
}
