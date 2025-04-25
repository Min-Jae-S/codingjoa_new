package com.codingjoa.security.oauth2;

import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.util.StringUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class OAuth2Attributes {
	
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String id;
	private String email;
	private String nickname;
	private String provider;
	
	@Builder
	private OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String id, String email,
			String nickname, String provider) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.provider = provider;
	}
	
	public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		OAuth2Attributes oAuth2Attributes = null;
		if (registrationId.equals("kakao")) {
			oAuth2Attributes = ofKakao(userNameAttributeName, attributes);
		} else if (registrationId.equals("naver")) {
			oAuth2Attributes = ofNaver(userNameAttributeName, attributes);
		} else if (registrationId.equals("google")) {
			oAuth2Attributes = ofGoogle(userNameAttributeName, attributes);
		} else {
			OAuth2Error oAuth2Error = new OAuth2Error("invalid_registration_id", "지원하지 않는 소셜 로그인 유형입니다: " + registrationId, null);	
			throw new OAuth2AuthenticationException(oAuth2Error, oAuth2Error.toString());
		}
		
		validate(oAuth2Attributes);
		
		return oAuth2Attributes;
	}
	
/*
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
	        "is_email_valid": true,   
	        "is_email_verified": true,
	        "email": "sample@sample.com",
	    },
	    ...
	}
*/
	@SuppressWarnings("unchecked")
	private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
		return getBuilder(userNameAttributeName, attributes)
				.id(String.valueOf(attributes.get("id"))) // (String) attributes.get("id")
				.email((String) kakaoAccount.get("email"))
				.nickname((String) profile.get("nickname"))
				.provider("kakao")
				.build();
	}
	
/*
	{
		"resultcode" : "00",
		"message" : "success",
		"response" : {
			"id" : "UYTseremWvYIrBiwcqLJu7i04g94NbIlfeuEwerj",
			"nickname" : "크하하하하",
			"email" : "smj20228@naver.com",
			"name" : "서민재"
		}
	}	
*/
	@SuppressWarnings("unchecked")
	private static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return getBuilder(userNameAttributeName, attributes)
				.id(String.valueOf(attributes.get("id")))
				.email((String) response.get("email"))
				.nickname((String) response.get("nickname"))
				.provider("naver")
				.build();
	}
	
/*	
	{
	  "sub": "103707946781717949594",
	  "email": "dsds550@gmail.com",
	  "verified_email": true,
	  "name": "MinJae Suh",
	  "given_name": "MinJae",
	  "family_name": "Suh",
	  "picture": "https://lh3.googleusercontent.com/a/AEdFTp5OW4CIt13eg5K6j6v4Ma03Bm1dgtwKlpP3PUo7=s96-c",
	  "locale": "ko"
	}
*/		
	private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return getBuilder(userNameAttributeName, attributes)
				.id(String.valueOf(attributes.get("id")))
				.email((String) attributes.get("email"))
				.nickname((String) attributes.get("name"))
				.provider("google")
				.build();
	}
	
	private static OAuth2AttributesBuilder getBuilder(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuth2Attributes.builder()
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName);
	}
	
	private static void validate(OAuth2Attributes oAuth2Attributes) {
		if (!StringUtils.hasText(oAuth2Attributes.getId())) {
			OAuth2Error oAuth2Error = new OAuth2Error("missing_id", "OAuth2 인증 서버로부터 사용자 ID 정보를 받지 못했습니다.", null);	
			throw new OAuth2AuthenticationException(oAuth2Error, oAuth2Error.toString());
		}
		
		if (!StringUtils.hasText(oAuth2Attributes.getEmail())) {
			OAuth2Error oAuth2Error = new OAuth2Error("missing_email", "OAuth2 인증 서버로부터 사용자 이메일 정보를 받지 못했습니다.", null);	
			throw new OAuth2AuthenticationException(oAuth2Error, oAuth2Error.toString());
		}
		
		if (!StringUtils.hasText(oAuth2Attributes.getNickname())) {
			OAuth2Error oAuth2Error = new OAuth2Error("missing_nickname", "OAuth2 인증 서버로부터 사용자 닉네임 정보를 받지 못했습니다.", null);	
			throw new OAuth2AuthenticationException(oAuth2Error, oAuth2Error.toString());
		}
	}
	
}
