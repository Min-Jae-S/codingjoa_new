package com.codingjoa.security.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
		OAuth2User loadOAuth2User = delegate.loadUser(userRequest);

		return null;
	}
	
}
