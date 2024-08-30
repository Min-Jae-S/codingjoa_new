package com.codingjoa.security.oauth2.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import lombok.extern.slf4j.Slf4j;

/*
 * The OAuth2 protocol recommends using the state parameter to prevent CSRF attacks. 
 * During authentication, the application sends this parameter in the authentication request, 
 * and the OAuth2 provider returns the unchanged parameter in the OAuth2 callback. 
 * The application compares the value of the state parameter returned by the provider with the initial value it sent. 
 * If they do not match, the authentication request is rejected.
 * To achieve this flow, the application must store the state parameter somewhere 
 * so that it can later compare it with the value returned by the OAuth2 provider.
 */

@SuppressWarnings("unused")
@Slf4j
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	private static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "OAUTH2.AUTHORIZATION_REQUEST";
	private static final String CONTINUE_PARAM_COOKIE_NAME = "CONTINUE";
	
	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		log.info("## {}.loadAuthorizationRequest");
		
		String stateParameter = request.getParameter(OAuth2ParameterNames.STATE);
		if (stateParameter == null) {
			return null;
		}
		
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
		return authorizationRequests.get(stateParameter);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("## {}.saveAuthorizationRequest");
		
		// authorizationRequest resolved by the 'OAuth2AuthorizationRequestResolver'
		if (authorizationRequest == null) {
			this.removeAuthorizationRequest(request, response);
			return;
		}
		
		String state = authorizationRequest.getState();
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
		authorizationRequests.put(state, authorizationRequest);
		
		// will put authorizationRequests to cookie instead of session
		//request.getSession().setAttribute(this.sessionAttributeName, authorizationRequests);
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		log.info("## {}.removeAuthorizationRequest");
		String stateParameter = request.getParameter(OAuth2ParameterNames.STATE);
		if (stateParameter == null) {
			return null;
		}
		
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
		OAuth2AuthorizationRequest originalRequest = authorizationRequests.remove(stateParameter);
		
		if (!authorizationRequests.isEmpty()) {
			// ...
		} else {
			// ...
		}
		
		return originalRequest;
	}
	
	private Map<String, OAuth2AuthorizationRequest> getAuthorizationRequests(HttpServletRequest request) {
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = null;
		return authorizationRequests;
	}

}
