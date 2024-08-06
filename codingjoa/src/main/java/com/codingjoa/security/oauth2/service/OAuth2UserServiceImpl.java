package com.codingjoa.security.oauth2.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.util.JsonUtils;
import com.codingjoa.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * @@ kakao (https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api)
 *  - GET/POST, https://kapi.kakao.com/v2/user/me
 *  - header
 * 		> Authorization: Bearer ${ACCESS_TOKEN}
 * 		> Content-type: application/x-www-form-urlencoded;charset=utf-8
	{
		"id" : 3625815491,
		"connected_at" : "2024-07-17T05:59:35Z",
		"properties" : {
			"nickname" : "서민재"
		},
		"kakao_account" : {
			"profile_nickname_needs_agreement" : false,
			"profile" : {
				"nickname" : "서민재",
				"is_default_nickname" : false
			}
		}
	}
	
 * @@ naver (https://developers.naver.com/docs/login/profile/profile.md)
 * 	- GET, https://openapi.naver.com/v1/nid/me
 * 	- header
 * 		> Authorization: Bearer ${ACCESS_TOKEN}
 * 		> Content-type: application/x-www-form-urlencoded;charset=utf-8
	{
		"resultcode" : "00",
		"message" : "success",
		"response" : {
			"id" : "UYTs-zkmWvHlYIrBiwcqLJu7i04g94NbIlfeuEOl-Og",
			"email" : "smj20228@naver.com",
			"name" : "서민재"
		}
	}
 * 
 */

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private final MemberMapper memberMapper;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("## {}.loadUser", this.getClass().getSimpleName());
		log.info("\t > userRequest = {}", Utils.specifiyFields(userRequest));
		
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		log.info("\t > delegate to the {} for loading a user", delegate.getClass().getSimpleName());
		
		OAuth2User loadedOAuth2User = delegate.loadUser(userRequest);
		Map<String, Object> userAttributes = loadedOAuth2User.getAttributes();
		log.info("\t > userAttributes = {}", userAttributes);
		
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
		mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		log.info("\t > mappedAuthorities = {}", mappedAuthorities);
		
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		log.info("\t > userNameAttributeName = {}", userNameAttributeName); // id, response
		
		return new DefaultOAuth2User(mappedAuthorities, userAttributes, userNameAttributeName);
	}
	
}
