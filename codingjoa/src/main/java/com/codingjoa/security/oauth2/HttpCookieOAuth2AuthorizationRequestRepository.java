package com.codingjoa.security.oauth2;

import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.SerializationUtils;

import com.codingjoa.util.CookieUtils;

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

@Slf4j
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	private static final String AUTHORIZATION_REQUEST_COOKIE_NAME = "AUTHORIZATION_REQUEST";
	private static final long EXPIRE_SECONDS = 600;
	
	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		log.info("## {}.loadAuthorizationRequest");
		
		String stateParameter = request.getParameter(OAuth2ParameterNames.STATE);
		if (stateParameter == null) {
			return null;
		}
		
		return this.getAuthorizationRequest(request);
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

		// put authorizationRequests to cookie instead of session
		CookieUtils.addCookie(response, AUTHORIZATION_REQUEST_COOKIE_NAME, serialize(authorizationRequest), EXPIRE_SECONDS);
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
		log.info("## {}.removeAuthorizationRequest(request, response");
		String stateParameter = request.getParameter(OAuth2ParameterNames.STATE);
		if (stateParameter == null) {
			return null;
		}
		
		OAuth2AuthorizationRequest originalRequest = this.getAuthorizationRequest(request);
		CookieUtils.removeCookie(request, response, AUTHORIZATION_REQUEST_COOKIE_NAME);
		if (originalRequest == null) {
			return null;
		}
		
		return stateParameter.equals(originalRequest.getState()) ? originalRequest : null;
	}
	
	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		log.info("## {}.removeAuthorizationRequest(request)");
		return null;
	}
	
	private OAuth2AuthorizationRequest getAuthorizationRequest(HttpServletRequest request) {
		Cookie cookie = CookieUtils.getCookie(request, AUTHORIZATION_REQUEST_COOKIE_NAME);
		return (cookie == null) ? null : deserialize(cookie);
	}
	
	private static String serialize(Object object) {
		byte[] serializedBytes = SerializationUtils.serialize(object);
        return Base64.getUrlEncoder().encodeToString(serializedBytes);
    }
	
	private static OAuth2AuthorizationRequest deserialize(Cookie cookie) {
		byte[] decodedBytes = Base64.getUrlDecoder().decode(cookie.getValue());
		return (OAuth2AuthorizationRequest) SerializationUtils.deserialize(decodedBytes);
	}

}
