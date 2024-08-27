package com.codingjoa.security.oauth2;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@SuppressWarnings("unchecked")
@Getter
public class OAuth2Attributes {
	
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String provider;
	private String email;
	private String nickname;
	
	@Builder
	private OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String provider, String email, String nickname) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.provider = provider;
		this.email = email;
		this.nickname = nickname;
	}
	
	public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		if ("kakao".equals(registrationId)) {
			return ofKakao(userNameAttributeName, attributes);
		} else if ("naver".equals(registrationId)) {
			return ofNaver(userNameAttributeName, attributes);
		} else if ("google".equals(registrationId)) {
			return ofGoogle(userNameAttributeName, attributes);
		} else {
			return null;
		}
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
	private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
		return OAuth2Attributes.builder()
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
				.provider("kakao")
				.email((String) kakaoAccount.get("email"))
				.nickname((String) profile.get("nicknmae"))
				.build();
	}
	
/*
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
	private static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return OAuth2Attributes.builder()
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
				.provider("naver")
				.email((String) response.get("email"))
				.nickname((String) response.get("nicknmae"))
				.build();
	}
	
/*	
	{
	  "sub": "103707946781717949594",
	  "email": "dsds550@gmail.com",
	  "verified_email": true,
	  "name": "홍길동",
	  "given_name": "길동",
	  "family_name": "홍",
	  "picture": "https://lh3.googleusercontent.com/a/AEdFTp5OW4CIt13eg5K6j6v4Ma03Bm1dgtwKlpP3PUo7=s96-c",
	  "locale": "ko"
	}
*/		
	private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuth2Attributes.builder()
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
				.provider("google")
				.build();
	}
	
}
