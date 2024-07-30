package com.codingjoa.security.service;

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
		
		// authenticationRequest = oAuth2LoginToken
		OAuth2LoginAuthenticationToken oAuth2LoginToken = (OAuth2LoginAuthenticationToken) authentication;
		
		ClientRegistration clientRegistration = oAuth2LoginToken.getClientRegistration();
		log.info("\t > [authenticationRequest] clientRegistration = {}", clientRegistration);
		
		OAuth2AuthorizationExchange exchange = oAuth2LoginToken.getAuthorizationExchange();
		log.info("\t > [authenticationRequest] exchange = {}", exchange);
		
		if (exchange != null) {
			log.info("\t > [authenticationRequest] exchange.authorizationRequest = {}", exchange.getAuthorizationRequest());
			log.info("\t > [authenticationRequest] exchange.authorizationResponse = {}", exchange.getAuthorizationResponse());
		}
		
		OAuth2AccessToken oAuthAccessToken = oAuth2LoginToken.getAccessToken();
		log.info("\t > [authenticationRequest] oAuthAccessToken = {}", oAuthAccessToken);
		
		if (oAuthAccessToken != null) {
			log.info("\t > [authenticationRequest] oAuthAccessToken.tokenValue = {}", oAuthAccessToken.getTokenValue());
			log.info("\t > [authenticationRequest] oAuthAccessToken.tokenType = {}", oAuthAccessToken.getTokenType().getValue());
		}
		
		return null;
	}


}
