package com.codingjoa.security.oauth2.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class OAuth2CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		// TODO Auto-generated method stub
		return null;
	}

}
