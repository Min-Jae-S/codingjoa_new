package com.codingjoa.security.oauth2;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class OAuth2ClientProperties {
	
	private final Map<String, Registration> registration = new HashMap<>();
	private final Map<String, Provider> provider = new HashMap<>();
	
	@PostConstruct
	public void init() {
		
	}
	
	@Getter
	public static class Registration {
		
	}
	
	@Getter
	public static class Provider {
		
	}
	
}
