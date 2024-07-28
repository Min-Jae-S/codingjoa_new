package com.codingjoa.security.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
//	{
//	  "access_token" : "9_ZIEDk-TXLeWV89WCg6_H91BuCE1VI_AAAAAQoqJRAAAAGQ9-2vGU8FYMfcu4fs",
//	  "token_type" : "bearer",
//	  "refresh_token" : "e39yJyyKwRX9E5fUzjkGrVtO-Xp3kd14AAAAAgoqJRAAAAGQ9-2vFU8FYMfcu4fs",
//	  "expires_in" : 21599,
//	  "scope" : "profile_image profile_nickname",
//	  "refresh_token_expires_in" : 5183999
//	}
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("## {}.loadUser", this.getClass().getSimpleName());
		
		OAuth2AccessToken oAuth2AccessToken = userRequest.getAccessToken();
		log.info("\t > accessToken = {}", oAuth2AccessToken.getTokenValue());
		log.info("\t > tokenType = {}", oAuth2AccessToken.getTokenType().getValue());
		log.info("\t > issuedAt = {}", oAuth2AccessToken.getIssuedAt());
		log.info("\t > expiresAt = {}", oAuth2AccessToken.getExpiresAt());
		log.info("\t > scopes = {}", oAuth2AccessToken.getScopes());
		
		userRequest.getAdditionalParameters().forEach((key, value) -> {
			log.info("\t > additionalParams = {} : {}", key, value);
		});
		
		return null;
	}
	
}
