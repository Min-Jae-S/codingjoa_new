package com.codingjoa.obsolete;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails.UserInfoEndpoint;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.codingjoa.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";
	private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";
	private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";
	private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = 
			new ParameterizedTypeReference<Map<String, Object>>() {};
	private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
	private RestOperations restOperations;
	
	public OAuth2UserServiceImpl() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		this.restOperations = restTemplate;
	}

	// ref) OAuth2LoginAuthenticationProvider.authenticate() {
	//			...
	// 			OAuth2AccessToken accessToken = authorizationCodeAuthenticationToken.getAccessToken();
	//			Map<String, Object> additionalParameters = authorizationCodeAuthenticationToken.getAdditionalParameters();
	//
	//			OAuth2User oauth2User = this.userService.loadUser(new OAuth2UserRequest(
	//				loginAuthenticationToken.getClientRegistration(), accessToken, additionalParameters));
	//			...
	//		}
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("## {}.loadUser", this.getClass().getSimpleName());
		
		UserInfoEndpoint userInfoEndpoint = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint();
		log.info("\t > check userInfoEndpoint component before requesting userInfo");
		log.info("\t > authenticationMethod = {}", userInfoEndpoint.getAuthenticationMethod());
		
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
		
		/*
		 * # kakao (https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api)
		 *  - GET/POST, https://kapi.kakao.com/v2/user/me
		 *  - header
		 * 		> Authorization: Bearer ${ACCESS_TOKEN}
		 * 		> Content-type: application/x-www-form-urlencoded;charset=utf-8
		 * 
		 * # naver (https://developers.naver.com/docs/login/profile/profile.md)
		 * 	- GET, https://openapi.naver.com/v1/nid/me
		 * 	- header
		 * 		> Authorization: Bearer ${ACCESS_TOKEN}
		 */
		
		log.info("## request userInfo");
		RequestEntity<?> request = requestEntityConverter.convert(userRequest);
		log.info("\t > url = {}", request.getUrl());
		log.info("\t > method = {}", request.getMethod());
		
		HttpHeaders headers = request.getHeaders();
		log.info("\t > header = {}", headers.keySet());
		log.info("\t > header, authroization = {}", headers.get(HttpHeaders.AUTHORIZATION));
		log.info("\t > header, content-type = {}", headers.getContentType());

		ResponseEntity<Map<String, Object>> response;
		try {
			// restTemplate.exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType)
			response = restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
		} catch (OAuth2AuthorizationException e) {
			OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE);
			throw new OAuth2AuthenticationException(oauth2Error, e);
		} catch (RestClientException e) {
			OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE);
			throw new OAuth2AuthenticationException(oauth2Error, e);
		}
		
		Map<String, Object> userAttributes = response.getBody();
		log.info("{}", JsonUtils.formatJson(userAttributes));
		
		Set<GrantedAuthority> authorities = new LinkedHashSet<>();
		authorities.add(new OAuth2UserAuthority(userAttributes));
		
		OAuth2AccessToken token = userRequest.getAccessToken();
		for (String authority : token.getScopes()) {
			authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
		}

		return new DefaultOAuth2User(authorities, userAttributes, userNameAttributeName);
	}
	
}
