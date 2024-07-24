package com.codingjoa.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import com.codingjoa.security.oauth2.OAuth2Provider;

@ComponentScan("com.codingjoa.security.oauth2")
@Configuration
public class OAuth2Config {

	@Autowired
	private Environment env;
	
	@Bean
	public InMemoryClientRegistrationRepository mainClientRegistrationRepository() {
		List<ClientRegistration> registrations = Arrays.asList(kakaoClientRegistration(), naverClientRegistration());
		return new InMemoryClientRegistrationRepository(registrations);
	}

	@Bean
	public InMemoryClientRegistrationRepository subClientRegistrationRepository() {
		List<ClientRegistration> registrations = Arrays.asList(kakaoClientRegistration(true), naverClientRegistration(true));
		return new InMemoryClientRegistrationRepository(registrations);
	}
	
	private ClientRegistration kakaoClientRegistration() {
		return OAuth2Provider.KAKAO.getBuilder("kakao")
				.clientId(env.getProperty("security.oauth2.client.registration.kakao.client-id"))
				.clientSecret(env.getProperty("security.oauth2.client.registration.kakao.client-secret"))
				.build();
	}

	private ClientRegistration kakaoClientRegistration(boolean fromProperties) {
		Builder builder = OAuth2Provider.KAKAO.getBuilder("kakao")
				.clientId(env.getProperty("security.oauth2.client.registration.kakao.client-id"))
				.clientSecret(env.getProperty("security.oauth2.client.registration.kakao.client-secret"));
		if (fromProperties) {
			return builder
					.redirectUriTemplate(env.getProperty("security.oauth2.client.registration.kakao.redirect-uri"))
					.build();
		} else {
			return builder.build();
		}
	}

	private ClientRegistration naverClientRegistration() {
		return OAuth2Provider.NAVER.getBuilder("naver")
				.clientId(env.getProperty("security.oauth2.client.registration.naver.client-id"))
				.clientSecret(env.getProperty("security.oauth2.client.registration.naver.client-secret"))
				.build();
	}

	private ClientRegistration naverClientRegistration(boolean fromProperties) {
		Builder builder = OAuth2Provider.NAVER.getBuilder("naver")
				.clientId(env.getProperty("security.oauth2.client.registration.naver.client-id"))
				.clientSecret(env.getProperty("security.oauth2.client.registration.naver.client-secret"));
		if (fromProperties) {
			return builder
					.redirectUriTemplate(env.getProperty("security.oauth2.client.registration.naver.redirect-uri"))
					.build();
		} else {
			return builder.build();
		}
	}
}
