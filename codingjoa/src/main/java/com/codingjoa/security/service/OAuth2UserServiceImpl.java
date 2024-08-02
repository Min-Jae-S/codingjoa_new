package com.codingjoa.security.service;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails.UserInfoEndpoint;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";
	private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";
	private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";
	private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
	private RestOperations restOperations;
	
	public OAuth2UserServiceImpl() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		this.restOperations = restTemplate;
	}

	// ref) OAuth2LoginAuthenticationProvider.authenticate
	//		...
	// 		OAuth2AccessToken accessToken = authorizationCodeAuthenticationToken.getAccessToken();
	//		Map<String, Object> additionalParameters = authorizationCodeAuthenticationToken.getAdditionalParameters();
	//
	//		OAuth2User oauth2User = this.userService.loadUser(new OAuth2UserRequest(
	//				loginAuthenticationToken.getClientRegistration(), accessToken, additionalParameters));
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("## {}.loadUser", this.getClass().getSimpleName());
		
		UserInfoEndpoint userInfoEndpoint = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint();
		log.info("## check userInfoEndpoint component before requesting userInfo");
		
		String userInfoUri = userInfoEndpoint.getUri();
		log.info("\t > userInfoUri = {}", userInfoUri);
		
		if (!StringUtils.hasText(userInfoUri)) {
			OAuth2Error oAuth2Error = new OAuth2Error(MISSING_USER_INFO_URI_ERROR_CODE);
			throw new OAuth2AuthenticationException(oAuth2Error);
		}
		
		String userNameAttributeName = userInfoEndpoint.getUserNameAttributeName();
		log.info("\t > userNameAttributeName = {}", userNameAttributeName);
		
		if (!StringUtils.hasText(userNameAttributeName)) {
			OAuth2Error oAuth2Error = new OAuth2Error(MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE);
			throw new OAuth2AuthenticationException(oAuth2Error);
		}
		
		log.info("## request userInfo");
		RequestEntity<?> request = requestEntityConverter.convert(userRequest);
		ResponseEntity<Map<String, Object>> response;
		
		return null;
	}
	
}
