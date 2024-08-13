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
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.security.dto.OAuth2UserDto;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.util.Utils;

import lombok.extern.slf4j.Slf4j;

/* * @@ kakao (https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api)
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
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
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
		
		Map<String, Object> userAttributes = loadedOAuth2User.getAttributes();
		log.info("\t > userAttributes = {}", userAttributes);
		
		//return loadedOAuth2User;
		
		String email = null;
		Map<String, Object> map = memberMapper.findUserDetailsByEmail(email);
		
		if (map == null) {
			map = save(); // save new member
		}
		
		return modelMapper.map(map, PrincipalDetails.class);
	}
	
	private OAuth2User resolveOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
		mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		
		// kakao: id, naver: response
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		
		return new DefaultOAuth2User(mappedAuthorities, oAuth2User.getAttributes(), userNameAttributeName);
	}
	
	private Map<String, Object> save() {
		return null;
	}
	
}
