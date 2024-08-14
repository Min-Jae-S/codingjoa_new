package com.codingjoa.security.oauth2.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.codingjoa.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2LoginProvider implements AuthenticationProvider { // OAuth2LoginAuthenticationProvider
	
	private final OAuth2AuthorizationCodeAuthenticationProvider authorizationCodeAuthenticationProvider;
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
	
	public OAuth2LoginProvider(
			OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient,
			OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) {
		this.authorizationCodeAuthenticationProvider = new OAuth2AuthorizationCodeAuthenticationProvider(accessTokenResponseClient);
		this.oAuth2UserService = oAuth2UserService;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2LoginAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > starting authentication of the {}", authentication.getClass().getSimpleName());
		
		OAuth2LoginAuthenticationToken loginToken = (OAuth2LoginAuthenticationToken) authentication;
		
		OAuth2AuthorizationCodeAuthenticationToken authCodeToken = new OAuth2AuthorizationCodeAuthenticationToken(
				loginToken.getClientRegistration(), loginToken.getAuthorizationExchange());
		
		// request access token (authenticate authorization code)
		log.info("\t > request for accessToken"); 
		OAuth2AuthorizationCodeAuthenticationToken authenticatedAuthCodeToken =
				(OAuth2AuthorizationCodeAuthenticationToken) authorizationCodeAuthenticationProvider.authenticate(authCodeToken);
		log.info("\t > received accessToken response, authenticatedAuthCodeToken = {}", Utils.specifiyFields(authenticatedAuthCodeToken));
				
		// request user info
		OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(
				authenticatedAuthCodeToken.getClientRegistration(),
				authenticatedAuthCodeToken.getAccessToken(), 
				authenticatedAuthCodeToken.getAdditionalParameters());
		OAuth2User loadedUser = oAuth2UserService.loadUser(oAuth2UserRequest); // PrincipaDetails
		
		OAuth2LoginAuthenticationToken authenticatedLoginToken = new OAuth2LoginAuthenticationToken(
				loginToken.getClientRegistration(),
				loginToken.getAuthorizationExchange(),
				loadedUser,
				loadedUser.getAuthorities(), // authorities already mapped in OAuth2UserService (mappedAuthorites)
				authenticatedAuthCodeToken.getAccessToken(),
				authenticatedAuthCodeToken.getRefreshToken());
		
		return authenticatedLoginToken;
	}

}
