package com.codingjoa.security.service;

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

import com.codingjoa.util.JsonUtils;
import com.codingjoa.util.Utils;
import com.nimbusds.oauth2.sdk.token.AccessToken;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class OAuth2LoginProvider implements AuthenticationProvider { // OAuth2LoginAuthenticationProvider
	
	private final OAuth2AuthorizationCodeAuthenticationProvider authorizationCodeAuthenticationProvider;
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> userService;
	
	public OAuth2LoginProvider(
			OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient,
			OAuth2UserService<OAuth2UserRequest, OAuth2User> userService) {
		this.authorizationCodeAuthenticationProvider = new OAuth2AuthorizationCodeAuthenticationProvider(accessTokenResponseClient);
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2LoginAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > starting authentication of the {}", authentication.getClass().getSimpleName());
		
		OAuth2LoginAuthenticationToken oAuth2LoginToken = (OAuth2LoginAuthenticationToken) authentication;
		
		OAuth2AuthorizationCodeAuthenticationToken oAuth2AuthCodeToken = new OAuth2AuthorizationCodeAuthenticationToken(
				oAuth2LoginToken.getClientRegistration(), oAuth2LoginToken.getAuthorizationExchange());
		
		
		log.info("## request accessToken"); // authenticate authorization code 
		OAuth2AuthorizationCodeAuthenticationToken authenticatedOAuth2AuthCodeToken =
				(OAuth2AuthorizationCodeAuthenticationToken) authorizationCodeAuthenticationProvider.authenticate(oAuth2AuthCodeToken);
		
		OAuth2AccessToken accessToken = authenticatedOAuth2AuthCodeToken.getAccessToken();
		Map<String, Object> additionalParameters = authenticatedOAuth2AuthCodeToken.getAdditionalParameters();
		log.info("{}", JsonUtils.formatJson(accessToken));
		log.info("{}", JsonUtils.formatJson(additionalParameters));
		
		// generate OAuth2UserRequest using clientRegistration, accessToken, additionalParameters
		OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(
				authenticatedOAuth2AuthCodeToken.getClientRegistration(), accessToken, additionalParameters);
		
		log.info("## request userInfo");
		OAuth2User oauth2User = this.userService.loadUser(oAuth2UserRequest);
		
		return null;
	}

}
