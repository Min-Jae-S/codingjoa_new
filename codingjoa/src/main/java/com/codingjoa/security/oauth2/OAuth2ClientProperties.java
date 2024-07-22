package com.codingjoa.security.oauth2;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class OAuth2ClientProperties {
	
	private final Map<String, OAuth2ClientProperties.Registration> registration = new HashMap<>();
	private final Map<String, OAuth2ClientProperties.Provider> provider = new HashMap<>();
	
	@Getter
	public static class Registration {
		
	}
	
	@Getter
	public static class Provider {
		
	}
	
}
