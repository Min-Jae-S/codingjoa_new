package com.codingjoa.security.oauth2;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

/*
	@@ kakao (https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api)
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

@Getter
public class OAuth2Attributes {
	
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String proivder;
	private String email;
	private String nickname;
	
	@Builder
	private OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String email, String nickname) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
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
	
	private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuth2Attributes.builder()
				.build();
	}

	private static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuth2Attributes.builder()
				.build();
	}
	
	private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuth2Attributes.builder()
				.build();
	}
	
//	private String extractEmail(String provider, Map<String, Object> attributes) {
//		if (provider.equals("kakao")) {
//			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//			return (String) kakaoAccount.get("email");
//		} else if (provider.equals("naver")) {
//			Map<String, Object> response = (Map<String, Object>) attributes.get("response");
//			return (String) response.get("email");
//		} else {
//			return null;
//		}
//	}
//
//	private String extractNickname(String provider, Map<String, Object> attributes) {
//		if (provider.equals("kakao")) {
//			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//			Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
//			return (String) profile.get("nickname");
//		} else if (provider.equals("naver")) {
//			Map<String, Object> response = (Map<String, Object>) attributes.get("response");
//			return (String) response.get("nickname");
//		} else {
//			return null;
//		}
//	}
	
}
