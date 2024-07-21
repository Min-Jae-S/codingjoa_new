package com.codingjoa.security.test;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public enum OAuth2Provider {
	
	/*
	 * security.oauth2.kakao.authorize-url=https://kauth.kakao.com/oauth/authorize
	 * security.oauth2.kakao.token-url=https://kauth.kakao.com/oauth/token
	 * security.oauth2.kakao.member-url=https://kapi.kakao.com/v2/user/me
	 */
	
	KAKAO {

		@Override
		public Builder getBuilder(String registrationId) {
			return getBuilder(registrationId, ClientAuthenticationMethod.POST, DEFAULT_REDIRECT_URL)
					.authorizationUri("https://kauth.kakao.com/oauth/authorize")
					.tokenUri("https://kauth.kakao.com/oauth/token")
					.userInfoUri("https://kapi.kakao.com/v2/user/me")
					//.userNameAttributeName(IdTokenClaimNames.SUB);
					.clientName("kakao");
		}
		
	},
	
	/*
	 * security.oauth2.naver.authorize-url=https://nid.naver.com/oauth2.0/authorize
	 * security.oauth2.naver.token-url=https://nid.naver.com/oauth2.0/token
	 * security.oauth2.naver.member-url=https://openapi.naver.com/v1/nid/me
	 */
	
	NAVER {

		@Override
		public Builder getBuilder(String registrationId) {
			return getBuilder(registrationId, ClientAuthenticationMethod.POST, DEFAULT_REDIRECT_URL)
					.authorizationUri("https://nid.naver.com/oauth2.0/authorize")
					.tokenUri("https://nid.naver.com/oauth2.0/token")
					.userInfoUri("https://openapi.naver.com/v1/nid/me")
					//.userNameAttributeName(IdTokenClaimNames.SUB);
					.clientName("naver");
		}
		
	};
	
	private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/login/oauth2/code/{registrationId}";
	
	protected final ClientRegistration.Builder getBuilder(String registrationId, 
			ClientAuthenticationMethod method, String redirectUri) {
		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
		builder.clientAuthenticationMethod(method);
		builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
		builder.redirectUriTemplate(redirectUri);
		return builder;
	}
	
	public abstract ClientRegistration.Builder getBuilder(String registrationId);
	
}
