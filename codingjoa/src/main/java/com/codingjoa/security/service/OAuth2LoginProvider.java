package com.codingjoa.security.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2LoginProvider implements AuthenticationProvider { // OAuth2LoginAuthenticationProvider

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2LoginAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > starting authentication of the {}", authentication.getClass().getSimpleName());
		
		// ## OAuth2LoginAuthenticationFilter.attemptAuthentication() {
		// 		OAuth2LoginAuthenticationToken authenticationRequest = new OAuth2LoginAuthenticationToken(
		//			clientRegistration, new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse));
		//		...
		//
		//		OAuth2LoginAuthenticationToken authenticationResult =
		//			(OAuth2LoginAuthenticationToken) this.getAuthenticationManager().authenticate(authenticationRequest);
		// }
		
		OAuth2LoginAuthenticationToken oAuth2LoginToken = (OAuth2LoginAuthenticationToken) authentication;
		
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

}
