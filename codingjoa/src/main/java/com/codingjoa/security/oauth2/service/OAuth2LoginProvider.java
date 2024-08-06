package com.codingjoa.security.oauth2.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
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
		
		// authenticate authorization code
		log.info("## request accessToken"); 
		OAuth2AuthorizationCodeAuthenticationToken authenticatedAuthCodeToken =
				(OAuth2AuthorizationCodeAuthenticationToken) authorizationCodeAuthenticationProvider.authenticate(authCodeToken);
		
		OAuth2AccessToken accessToken = authenticatedAuthCodeToken.getAccessToken();
		Map<String, Object> additionalParameters = authenticatedAuthCodeToken.getAdditionalParameters();
		log.info("\t > accessToken = {}", Utils.specifiyFields(accessToken));
		log.info("\t > additionalParameters = {}", additionalParameters.keySet());
		
		log.info("## request userInfo"); 
		OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(
				authenticatedAuthCodeToken.getClientRegistration(), accessToken, additionalParameters);
		OAuth2User loadedOAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);
		log.info("## loadedOAuth2User = {}", loadedOAuth2User);
		log.info("\t > attributes = {}", loadedOAuth2User.getAttributes());
		
		Collection<? extends GrantedAuthority> mappedAuthorities = loadedOAuth2User.getAuthorities();
		log.info("\t > mappedAuthorities = {}", mappedAuthorities);
		
		OAuth2LoginAuthenticationToken authenticatedLoginToken = new OAuth2LoginAuthenticationToken(
				loginToken.getClientRegistration(),
				loginToken.getAuthorizationExchange(),
				loadedOAuth2User,
				mappedAuthorities,
				accessToken,
				authenticatedAuthCodeToken.getRefreshToken());
		
		Object details = loginToken.getDetails();
		log.info("\t > details = {}", details);
		authenticatedLoginToken.setDetails(details);
		
		return authenticatedLoginToken;
	}

}
