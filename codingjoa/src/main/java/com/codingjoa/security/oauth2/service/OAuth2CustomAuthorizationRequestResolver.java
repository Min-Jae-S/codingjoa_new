package com.codingjoa.security.oauth2.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import com.codingjoa.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
	
	private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;
	
	public OAuth2CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository,
													String authorizationRequestBaseUri) {
		this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
				clientRegistrationRepository, authorizationRequestBaseUri);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
		return customize(request, authorizationRequest);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
		return customize(request, authorizationRequest);
	}
	
	private OAuth2AuthorizationRequest customize(HttpServletRequest request, OAuth2AuthorizationRequest authorizationRequest) {
		log.info("## {}.customize", this.getClass().getSimpleName());
		log.info("\t > original authorizationRequest = {}", JsonUtils.formatJson(authorizationRequest));
		
		if (authorizationRequest == null) {
			return null;
		}
		
		return null;
	}

}
