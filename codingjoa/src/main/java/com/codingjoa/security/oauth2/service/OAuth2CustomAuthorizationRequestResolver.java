package com.codingjoa.security.oauth2.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.codingjoa.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
	
	private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;
	
	public OAuth2CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository,
													String authorizationRequestBaseUri) {
		this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository,
				authorizationRequestBaseUri);
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
		
		String authorizationRequestUri = customizeAuthorizationRequestUri(authorizationRequest);
		
		Map<String, Object> additionalParams = new HashMap<String, Object>(authorizationRequest.getAdditionalParameters());
		additionalParams.put("continue", resolveContinueUrl(request));
		
		OAuth2AuthorizationRequest customizedAuthorizationRequest = OAuth2AuthorizationRequest.from(authorizationRequest)
				.authorizationRequestUri(authorizationRequestUri)
				.additionalParameters(additionalParams)
				.build();
		log.info("\t > customized authorizationRequest = {}", JsonUtils.formatJson(customizedAuthorizationRequest));
		
		return customizedAuthorizationRequest;
	}
	
	private String customizeAuthorizationRequestUri(OAuth2AuthorizationRequest authorizationRequest) {
		// https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=a4bfed5a0f06c18566476fdb677bd1b6&state=zVJpDVYxZG33s7R9u6Ig7SfEXP9BReYvygoOEEjofPA%3D&redirect_uri=http://localhost:8888/codingjoa/login/kakao/callback"
		String authorizationRequestUri = authorizationRequest.getAuthorizationRequestUri();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authorizationRequestUri);
		String redirectUriParam = builder.build().getQueryParams().getFirst("redirect_uri");
		builder.replaceQueryParam("redirect_uri", restoreAndEncode(redirectUriParam));
		
		String registrationId = (String) authorizationRequest.getAttribute(OAuth2ParameterNames.REGISTRATION_ID);
		if (registrationId.equals("kakao")) {
			builder.queryParam("prompt", "login");
		}
		
		return builder.build().toUriString();
	}
	
	private String restoreAndEncode(String value) {
		String restoredUri = UriUtils.decode(value, StandardCharsets.UTF_8);
		return UriUtils.encode(restoredUri, StandardCharsets.UTF_8);
	}
	
	private String resolveContinueUrl(HttpServletRequest request) {
		String continueUrl = request.getParameter("continue");
		log.info("\t > continue = {}", continueUrl);
		
		return UriUtils.encode(continueUrl, StandardCharsets.UTF_8);
	}

}
