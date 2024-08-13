package com.codingjoa.security.oauth2.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.security.dto.OAuth2UserDto;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.util.Utils;

import lombok.extern.slf4j.Slf4j;

/*
	@@ kakao (https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api)
		- GET/POST, https://kapi.kakao.com/v2/user/me
		- header
			> Authorization: Bearer ${ACCESS_TOKEN}
			> Content-type: application/x-www-form-urlencoded;charset=utf-8
		- body
		{
		    "id":123456789,
		    "connected_at": "2022-04-11T01:45:28Z",
		    "kakao_account": { 
		        "profile_nickname_needs_agreement" : false,
		        "profile_image_needs_agreement" : false,
		        "profile": {
		            "nickname": "홍길동",
		            "thumbnail_image_url": "http://yyy.kakao.com/.../img_110x110.jpg",
		            "profile_image_url": "http://yyy.kakao.com/dn/.../img_640x640.jpg",
		            "is_default_image":false,
		            "is_default_nickname": false
		        },
		        "name_needs_agreement":false, 
		        "name":"홍길동",
		        "email_needs_agreement":false, 
		        "is_email_valid": true,   
		        "is_email_verified": true,
		        "email": "sample@sample.com",
		    },
		    "properties":{
		        "${CUSTOM_PROPERTY_KEY}": "${CUSTOM_PROPERTY_VALUE}",
		        ...
		    },
		    "for_partner": {
		        "uuid": "${UUID}"
		    }
		}
	
 	@@ naver (https://developers.naver.com/docs/login/profile/profile.md)
 		- GET, https://openapi.naver.com/v1/nid/me
  		- header
 			> Authorization: Bearer ${ACCESS_TOKEN}
 			> Content-type: application/x-www-form-urlencoded;charset=utf-8
 		- body
		{
			"resultcode" : "00",
			"message" : "success",
			"response" : {
				"id" : "UYTs-zkmWvHlYIrBiwcqLJu7i04g94NbIlfeuEOl-Og",
				"email" : "smj20228@naver.com",
				"name" : "서민재"
			}
		}
		
 */

@SuppressWarnings({ "unused", "unchecked" })
@Slf4j
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private static final String INVALID_PROVIDER_ERROR_CODE = "invalid_provider";
	private static final String MISSING_EMAIL_RESPONSE_ERROR_CODE = "missing_email_response";
	private final MemberMapper memberMapper;
	private final ModelMapper modelMapper;
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
	
	@Autowired
	public OAuth2UserServiceImpl(MemberMapper memberMapper, @Qualifier("customModelMapper") ModelMapper modelMapper) {
		this.memberMapper = memberMapper;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("## {}.loadUser", this.getClass().getSimpleName());
		log.info("\t > request for userInfo");
		log.info("\t > delegate to the {} for loading a user", delegate.getClass().getSimpleName());
		
		OAuth2User loadedOAuth2User = delegate.loadUser(userRequest);
		log.info("\t > received userInfo response, loadedOAuth2User = {}", Utils.specifiyFields(loadedOAuth2User));
		
		Map<String, Object> attributes = loadedOAuth2User.getAttributes();
		log.info("\t > {}", Utils.formatPrettyJson(attributes));
		
		String provider =  userRequest.getClientRegistration().getRegistrationId();
		String attributeKeyName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
				.getUserNameAttributeName();
		log.info("\t > provider = {}, attributeKeyName = {}", provider, attributeKeyName); // kakao:id, naver:response
		
		String memberEmail = extractEmail(provider, attributes);
		log.info("\t > extracted email = {}", memberEmail);
		
		if (memberEmail == null) {
			throw new OAuth2AuthenticationException(new OAuth2Error(MISSING_EMAIL_RESPONSE_ERROR_CODE));
		}
		
		Map<String, Object> userDetailsMap = memberMapper.findUserDetailsByEmail(memberEmail);
		
		if (userDetailsMap == null) {
			userDetailsMap = save();
		}
		
		PrincipalDetails principalDetails = modelMapper.map(userDetailsMap, PrincipalDetails.class);
		//log.info("\t > principalDetails = {}", Utils.formatPrettyJson(principalDetails));
		
		return principalDetails;
	}
	
	private OAuth2User resolveOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
		mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		
		return new DefaultOAuth2User(mappedAuthorities, oAuth2User.getAttributes(), userNameAttributeName);
	}
	
	private Map<String, Object> save() {
		return null;
	}
	
	private String extractEmail(String provider, Map<String, Object> attributes) {
		if (provider.equals("kakao")) {
			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
			return (String) kakaoAccount.get("email");
		} else if (provider.equals("naver")) {
			Map<String, Object> response = (Map<String, Object>) attributes.get("response");
			return (String) response.get("email");
		} else {
			throw new OAuth2AuthenticationException(new OAuth2Error(INVALID_PROVIDER_ERROR_CODE));
		}
		
	}
	
}
