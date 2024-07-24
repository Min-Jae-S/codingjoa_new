package com.codingjoa.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import com.codingjoa.security.oauth2.OAuth2Provider;

@Configuration
public class OAuth2Config {

	@Autowired
	private Environment env;
	
	@Bean(name = "mainClientRegistrationRepository")
	public InMemoryClientRegistrationRepository mainClientRegistrationRepository() {
		List<ClientRegistration> registrations = Arrays.asList(kakaoClientRegistration(), naverClientRegistration());
		return new InMemoryClientRegistrationRepository(registrations);
	}

	@Bean(name = "subClientRegistrationRepository")
	public InMemoryClientRegistrationRepository subClientRegistrationRepository() {
		List<ClientRegistration> registrations = Arrays.asList(kakaoClientRegistration(false), naverClientRegistration(false));
		return new InMemoryClientRegistrationRepository(registrations);
	}
	
	private ClientRegistration kakaoClientRegistration() {
		return OAuth2Provider.KAKAO.getBuilder("kakao")
				.clientId(env.getProperty("security.oauth2.client.registration.kakao.client-id"))
				.clientSecret(env.getProperty("security.oauth2.client.registration.kakao.client-secret"))
				.build();
	}

	private ClientRegistration kakaoClientRegistration(boolean useRedirectUriTemplate) {
		return useRedirectUriTemplate ? kakaoClientRegistration()
				: OAuth2Provider.KAKAO.getBuilder("kakao")
						.clientId(env.getProperty("security.oauth2.client.registration.kakao.client-id"))
						.clientSecret(env.getProperty("security.oauth2.client.registration.kakao.client-secret"))
						.redirectUriTemplate(env.getProperty("security.oauth2.client.registration.kakao.redirect-uri"))
						.build();
	
	}
	
	private ClientRegistration naverClientRegistration() {
		return OAuth2Provider.NAVER.getBuilder("naver")
				.clientId(env.getProperty("security.oauth2.client.registration.naver.client-id"))
				.clientSecret(env.getProperty("security.oauth2.client.registration.naver.client-secret"))
				.build();
	}

	private ClientRegistration naverClientRegistration(boolean useRedirectUriTemplate) {
		return useRedirectUriTemplate ? naverClientRegistration()
				: OAuth2Provider.NAVER.getBuilder("naver")
					.clientId(env.getProperty("security.oauth2.client.registration.naver.client-id"))
					.clientSecret(env.getProperty("security.oauth2.client.registration.naver.client-secret"))
					.redirectUriTemplate(env.getProperty("security.oauth2.client.registration.naver.redirect-uri"))
					.build();
	}
}
