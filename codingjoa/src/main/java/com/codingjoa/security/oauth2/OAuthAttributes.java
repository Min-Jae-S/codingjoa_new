package com.codingjoa.security.oauth2;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
	
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String email;
	private String nickname;
	
	@Builder
	private OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String email, String nickname) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.email = email;
		this.nickname = nickname;
	}
	
	public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
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
	
	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
				.build();
	}

	private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
				.build();
	}
	
	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
				.build();
	}
}
