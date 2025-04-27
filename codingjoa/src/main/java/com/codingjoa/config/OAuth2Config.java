package com.codingjoa.config;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import com.codingjoa.security.oauth2.OAuth2CustomProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class OAuth2Config {

	private final Environment env;
	
	@Primary
	@Bean(name = "clientRegistrationRepository")
	public ClientRegistrationRepository clientRegistrationRepository() {
		log.info("## clientRegistrationRepository");
		List<ClientRegistration> registrations = Arrays.asList(kakaoClientRegistration(), naverClientRegistration(), googleClientRegistration());
		return new InMemoryClientRegistrationRepository(registrations);
	}

	@Bean(name = "testClientRegistrationRepository")
	public ClientRegistrationRepository testClientRegistrationRepository() {
		List<ClientRegistration> registrations = Arrays.asList("kakao", "naver", "google")
				.stream()
				.map(registrationId -> getClientRegistration(registrationId))
				.collect(Collectors.toList());
		return new InMemoryClientRegistrationRepository(registrations);
	}
	
	@Bean
	public OAuth2AuthorizedClientService oAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
	}
	
	@Bean
	public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
		return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(oAuth2AuthorizedClientService);
	}
	
	@Bean
	public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
		return new DefaultAuthorizationCodeTokenResponseClient();
	}
	
	private ClientRegistration kakaoClientRegistration() {
		return OAuth2CustomProvider.KAKAO.getBuilder("kakao")
				.clientId(env.getProperty("security.oauth2.client.registration.kakao.client-id"))
				.clientSecret(env.getProperty("security.oauth2.client.registration.kakao.client-secret"))
				.build();
	}
	
	private ClientRegistration naverClientRegistration() {
		return OAuth2CustomProvider.NAVER.getBuilder("naver")
				.clientId(env.getProperty("security.oauth2.client.registration.naver.client-id"))
				.clientSecret(env.getProperty("security.oauth2.client.registration.naver.client-secret"))
				.build();
	}
	
//	private ClientRegistration googleClientRegistration() {
//		return OAuth2CustomProvider.GOOGLE.getBuilder("google")
//				.clientId(env.getProperty("security.oauth2.client.registration.google.client-id"))
//				.clientSecret(env.getProperty("security.oauth2.client.registration.google.client-secret"))
//				.scope(env.getProperty("security.oauth2.client.registration.google.scope"))
//				.build();
//	}
	
	private ClientRegistration googleClientRegistration() {
		return CommonOAuth2Provider.GOOGLE.getBuilder("google")
				.clientId(env.getProperty("security.oauth2.client.registration.google.client-id"))
				.clientSecret(env.getProperty("security.oauth2.client.registration.google.client-secret"))
				.redirectUriTemplate(env.getProperty("security.oauth2.client.registration.google.redirect-uri-template"))
				.build();
	}

	@SuppressWarnings("unchecked")
	private ClientRegistration getClientRegistration(String registrationId) {
		String registrationKey = "security.oauth2.client.registration." + registrationId + ".";
		String providerKey = "security.oauth2.client.provider." + registrationId + ".";
		
		return ClientRegistration.withRegistrationId(registrationId)
				.clientId(env.getProperty(registrationKey + "client-id"))
				.clientSecret(env.getProperty(registrationKey + "client-secret"))
				.clientName(env.getProperty(registrationKey + "client-name"))
				.redirectUriTemplate(env.getProperty(registrationKey + "redirect-uri-template"))
				.authorizationGrantType(new AuthorizationGrantType(env.getProperty(registrationKey + "authorization-grant-type")))
				.scope(env.getProperty(registrationKey + "scope", Set.class))
				.authorizationUri(env.getProperty(providerKey + "authorization-uri"))
				.tokenUri(env.getProperty(providerKey + "token-uri"))
				.userInfoUri(env.getProperty(providerKey + "user-info-uri"))
				.userNameAttributeName(env.getProperty(providerKey + "user-name-attribute"))
				.build();
	}
	
}
