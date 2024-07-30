package com.codingjoa.security.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Component
public class OAuth2LoginProvider implements AuthenticationProvider { // OAuth2LoginAuthenticationProvider
	
	@Autowired(required = false)
	private OAuth2UserService<OAuth2UserRequest, OAuth2User> userService;
	
	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2LoginAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > starting authentication of the {}", authentication.getClass().getSimpleName());
		
		OAuth2LoginAuthenticationToken oAuth2LoginToken = (OAuth2LoginAuthenticationToken) authentication;
		//checkOAuth2LoginToken(oAuth2LoginToken);
		
		OAuth2AuthorizationCodeAuthenticationToken authorizationCodeAuthenticationToken;
		
		return null;
	}
	
	private List<String> getFieldNames(Object object) {
		if (object == null) {
			return null;
		}
		
		Field[] fields = object.getClass().getDeclaredFields();
		return Arrays.stream(fields)
				.map(field -> field.getName())
				.collect(Collectors.toList());
	}
	
	private void checkOAuth2LoginToken(OAuth2LoginAuthenticationToken oAuth2LoginToken) {
		ClientRegistration clientRegistration = oAuth2LoginToken.getClientRegistration();
		log.info("\t > clientRegistration = {}", getFieldNames(clientRegistration));
		
		OAuth2AuthorizationExchange oAuth2Exchange = oAuth2LoginToken.getAuthorizationExchange();
		log.info("\t > oAuth2Exchange = {}", getFieldNames(oAuth2Exchange));
		
		// from back-end session
		log.info("\t > oAuth2AuthorizationRequest = {}", getFieldNames(oAuth2Exchange.getAuthorizationRequest()));
		
		// from authorization server
		log.info("\t > oAuth2AuthorizationResponse = {}", getFieldNames(oAuth2Exchange.getAuthorizationResponse()));
		
		OAuth2AccessToken oAuthAccessToken = oAuth2LoginToken.getAccessToken();
		log.info("\t > oAuth2AccessToken = {}", getFieldNames(oAuthAccessToken));
		log.info("\t > oAuth2Details = {}", getFieldNames(oAuth2LoginToken.getDetails()));
	}

}
