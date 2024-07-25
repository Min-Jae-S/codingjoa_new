package com.codingjoa.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import com.codingjoa.security.oauth2.OAuth2ClientProperties;
import com.codingjoa.security.oauth2.OAuth2Provider;

@ComponentScan("com.codingjoa.security.oauth2")
@Configuration
public class OAuth2Config {

	@Autowired
	private Environment env;
	
	@Bean(name = "mainClientRegistrationRepository")
	public InMemoryClientRegistrationRepository mainClientRegistrationRepository() {
		List<ClientRegistration> registrations = Arrays.asList(kakaoClientRegistration(), naverClientRegistration());
		return new InMemoryClientRegistrationRepository(registrations);
	}

	@Bean(name = "testClientRegistrationRepository")
	public InMemoryClientRegistrationRepository testClientRegistrationRepository() {
		List<ClientRegistration> registrations = Arrays.asList("kakao", "naver")
				.stream()
				.map(registrationId -> getClientRegistration(registrationId))
				.collect(Collectors.toList());
		return new InMemoryClientRegistrationRepository(registrations);
	}
	
	private ClientRegistration kakaoClientRegistration() {
		return OAuth2Provider.KAKAO.getBuilder("kakao")
				.clientId(env.getProperty("security.oauth2.client.registration.kakao.client-id"))
				.clientSecret(env.getProperty("security.oauth2.client.registration.kakao.client-secret"))
				.build();
	}
	
	private ClientRegistration naverClientRegistration() {
		return OAuth2Provider.NAVER.getBuilder("naver")
				.clientId(env.getProperty("security.oauth2.client.registration.naver.client-id"))
				.clientSecret(env.getProperty("security.oauth2.client.registration.naver.client-secret"))
				.build();
	}

	private ClientRegistration getClientRegistration(String registrationId) {
		String registrationKey = "security.oauth2.client.registration." + registrationId + ".";
		String providerKey = "security.oauth2.client.provider." + registrationId + ".";
		
		return ClientRegistration.withRegistrationId(registrationId)
				.clientId(env.getProperty(registrationKey + "client-id"))
				.clientSecret(env.getProperty(registrationKey + "client-secret"))
				.clientName(env.getProperty(registrationKey + "client-name"))
				.redirectUriTemplate(env.getProperty(registrationKey + "redirect-uri-template"))
				.authorizationGrantType(new AuthorizationGrantType(env.getProperty(registrationKey + "authorization-grant-type")))
				.authorizationUri(env.getProperty(providerKey + "authorization-uri"))
				.tokenUri(env.getProperty(providerKey + "token-uri"))
				.userInfoUri(env.getProperty(providerKey + "user-info-uri"))
				.build();
	}
	
	//OAuth2ClientPropertiesRegistrationAdapter.getRegistrations(OAuth2ClientProperties properties)
	@SuppressWarnings("unused")
	private Map<String, ClientRegistration> getClientRegistrations(OAuth2ClientProperties properties) {
		Map<String, ClientRegistration> clientRegistrations = new HashMap<>();
		properties.getRegistration().forEach((key, value) -> {
			
		});
		
		return clientRegistrations;
	}
	
}
