package com.codingjoa.security.oauth2.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.codingjoa.entity.Auth;
import com.codingjoa.entity.Member;
import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.util.Utils;

import lombok.RequiredArgsConstructor;
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
				"nickname" : "크하하하하",
				"email" : "smj20228@naver.com",
				"name" : "서민재"
			}
		}
		
 */

@SuppressWarnings({ "unused", "unchecked" })
@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private static final String MISSING_EMAIL_RESPONSE_ERROR_CODE = "missing_email_response";
	private static final String MISSING_NICKNAME_RESPONSE_ERROR_CODE = "missing_nickname_response";
	private final MemberMapper memberMapper;
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("## {}.loadUser", this.getClass().getSimpleName());
		log.info("\t > request for userInfo");
		log.info("\t > delegate to the {} for loading a user", delegate.getClass().getSimpleName());
		
		OAuth2User loadedOAuth2User = delegate.loadUser(userRequest);
		Map<String, Object> attributes = loadedOAuth2User.getAttributes();
		log.info("\t > received userInfo response {}", Utils.formatPrettyJson(attributes));
		
		String provider = userRequest.getClientRegistration().getRegistrationId();
		String attributeKeyName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
				.getUserNameAttributeName();
		log.info("\t > provider = {}, attributeKeyName = {}", provider, attributeKeyName); // kakao:id, naver:response
		
		String email = extractEmail(provider, attributes);
		log.info("\t > email from attributes = {}", email);
		
		if (email == null) {
			OAuth2Error oAuth2Error = new OAuth2Error(MISSING_EMAIL_RESPONSE_ERROR_CODE, 
					"An error occurred while attempting to extract 'email' from attibutes", null);
			throw new OAuth2AuthenticationException(oAuth2Error, oAuth2Error.toString());
		}
		
		Map<String, Object> userDetailsMap = memberMapper.findUserDetailsByEmail(email);
		
		if (userDetailsMap == null) {
			log.info("\t > not a registered member, proceed with the registration");

			String nickname = extractNickname(provider, attributes);
			log.info("\t > nickname from attributes = {}", nickname);
			
			if (nickname == null) {
				OAuth2Error oAuth2Error = new OAuth2Error(MISSING_NICKNAME_RESPONSE_ERROR_CODE, 
						"An error occurred while attempting to extract 'nickname' from attibutes", null);
				throw new OAuth2AuthenticationException(oAuth2Error, oAuth2Error.toString());
			}
			
			while (memberMapper.isNicknameExist(nickname)) {
				if (nickname.length() > 6) {
					 nickname = nickname.substring(0, 6);
				}
				nickname = nickname + RandomStringUtils.randomNumeric(4);
				log.info("\t > create new nickname due to conflict: {}", nickname);
			}
			
			Member member = Member.builder()
					.memberEmail(email)
					.memberNickname(nickname)
					.memberAgree(false)
					.build();
			memberMapper.insertMember(member);
			
			Integer memberIdx = member.getMemberIdx();
			Auth auth = Auth.builder()
					.memberIdx(memberIdx)
					.memberRole("ROLE_MEMBER")
					.build();
			memberMapper.insertAuth(auth);
			
			userDetailsMap = memberMapper.findUserDetailsByIdx(memberIdx);
		} else {
			log.info("\t > already a registered member");
		}
		
		return PrincipalDetails.from(userDetailsMap);
	}
	
	private OAuth2User resolveOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
		mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		
		return new DefaultOAuth2User(mappedAuthorities, oAuth2User.getAttributes(), userNameAttributeName);
	}
	
	private String extractEmail(String provider, Map<String, Object> attributes) {
		if (provider.equals("kakao")) {
			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
			return (String) kakaoAccount.get("email");
		} else if (provider.equals("naver")) {
			Map<String, Object> response = (Map<String, Object>) attributes.get("response");
			return (String) response.get("email");
		} else {
			return null;
		}
	}

	private String extractNickname(String provider, Map<String, Object> attributes) {
		if (provider.equals("kakao")) {
			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
			Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
			return (String) profile.get("nickname");
		} else if (provider.equals("naver")) {
			Map<String, Object> response = (Map<String, Object>) attributes.get("response");
			return (String) response.get("nickname");
		} else {
			return null;
		}
	}
	
}
