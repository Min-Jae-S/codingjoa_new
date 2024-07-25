package com.codingjoa.security.oauth2;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public enum OAuth2Provider {
	
	/*
	 * security.oauth2.client.provider.kakao.authorization-uri=https://kauth.kakao.com/oauth/authorize
	 * security.oauth2.client.provider.kakao.token-uri=https://kauth.kakao.com/oauth/token
	 * security.oauth2.client.provider.kakao.user-info-uri=https://kapi.kakao.com/v2/user/me
	 */	
	KAKAO {

		@Override
		public Builder getBuilder(String registrationId) {
			return getBuilder(registrationId, ClientAuthenticationMethod.POST, DEFAULT_REDIRECT_URL)
					.authorizationUri("https://kauth.kakao.com/oauth/authorize")
					.tokenUri("https://kauth.kakao.com/oauth/token")
					.userInfoUri("https://kapi.kakao.com/v2/user/me")
					//.userNameAttributeName(IdTokenClaimNames.SUB);
					.clientName("Kakao");
		}
		
	},
	
	/*
	 * security.oauth2.client.provider.naver.authorization-uri=https://nid.naver.com/oauth2.0/authorize
	 * security.oauth2.client.provider.naver.token-uri=https://nid.naver.com/oauth2.0/token
	 * security.oauth2.client.provider.naver.user-info-uri=https://openapi.naver.com/v1/nid/me
	 */	
	NAVER {

		@Override
		public Builder getBuilder(String registrationId) {
			return getBuilder(registrationId, ClientAuthenticationMethod.POST, DEFAULT_REDIRECT_URL)
					.authorizationUri("https://nid.naver.com/oauth2.0/authorize")
					.tokenUri("https://nid.naver.com/oauth2.0/token")
					.userInfoUri("https://openapi.naver.com/v1/nid/me")
					//.userNameAttributeName(IdTokenClaimNames.SUB);
					.clientName("Naver");
		}
		
	};
	
	//private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";
	private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/{registrationId}/callback";
	
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
