package com.codingjoa.obsolete;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import com.codingjoa.obsolete.OAuth2ClientProperties;
import com.codingjoa.obsolete.OAuth2ClientProperties.Provider;
import com.codingjoa.security.oauth2.OAuth2CustomProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
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
		registrations.forEach(registration -> 
			log.info("\t > registrationId: {}, redirectUriTemplate: {}", registration.getRegistrationId(), registration.getRedirectUriTemplate()));
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
				.userNameAttributeName(env.getProperty(providerKey + "user-name-attribute"))
				.build();
	}
	
	// OAuth2ClientPropertiesRegistrationAdapter.getRegistrations
	// OAuth2ClientPropertiesMapper.asClientRegistrations
	private Map<String, ClientRegistration> getClientRegistrations(OAuth2ClientProperties properties) {
		Map<String, ClientRegistration> clientRegistrations = new HashMap<>();
		properties.getRegistration().forEach((key, value) -> { // Map<String, Registration> 
			clientRegistrations.put(key, getClientRegistration(key, value, properties.getProvider()));
		});
		
		return clientRegistrations;
	}
	
	// OAuth2ClientPropertiesMapper.getClientRegistration
	private ClientRegistration getClientRegistration(String registrationId,
			OAuth2ClientProperties.Registration properties, Map<String, Provider> providers) {
//		Builder builder = getBuilderFromIssuerIfPossible(registrationId, properties.getProvider(), providers);
//		if (builder == null) {
//			builder = getBuilder(registrationId, properties.getProvider(), providers);
//		}
//		PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
//		map.from(properties::getClientId).to(builder::clientId);
//		map.from(properties::getClientSecret).to(builder::clientSecret);
//		map.from(properties::getClientAuthenticationMethod)
//			.as(ClientAuthenticationMethod::new)
//			.to(builder::clientAuthenticationMethod);
//		map.from(properties::getAuthorizationGrantType)
//			.as(AuthorizationGrantType::new)
//			.to(builder::authorizationGrantType);
//		map.from(properties::getRedirectUri).to(builder::redirectUri);
//		map.from(properties::getScope).as(StringUtils::toStringArray).to(builder::scope);
//		map.from(properties::getClientName).to(builder::clientName);
//		
//		return builder.build();
		return null;
	}
	
	// OAuth2ClientPropertiesMapper.getBuilderFromIssuerIfPossible
	private Builder getBuilderFromIssuerIfPossible(String registrationId, String configuredProviderId,
			Map<String, Provider> providers) {
//		String providerId = (configuredProviderId != null) ? configuredProviderId : registrationId;
//		if (providers.containsKey(providerId)) {
//			Provider provider = providers.get(providerId);
//			String issuer = provider.getIssuerUri();
//			if (issuer != null) {
//				Builder builder = ClientRegistrations.fromIssuerLocation(issuer).registrationId(registrationId);
//				return getBuilder(builder, provider);
//			}
//		}
		return null;
	}
	
	// OAuth2ClientPropertiesMapper.getBuilder
	private Builder getBuilder(String registrationId, String configuredProviderId, Map<String, Provider> providers) {
		String providerId = (configuredProviderId != null) ? configuredProviderId : registrationId;
		
		CommonOAuth2Provider provider = getCommonProvider(providerId);
		if (provider == null && !providers.containsKey(providerId)) {
			throw new IllegalStateException(getErrorMessage(configuredProviderId, registrationId));
		}
		
		Builder builder = (provider != null) ? provider.getBuilder(registrationId)
				: ClientRegistration.withRegistrationId(registrationId);
		if (providers.containsKey(providerId)) {
			return getBuilder(builder, providers.get(providerId));
		}
		
		return builder;
	}
	
	// OAuth2ClientPropertiesMapper.getBuilder
	private Builder getBuilder(Builder builder, Provider provider) {
		builder.authorizationUri(provider.getAuthorizationUri());
		builder.tokenUri(provider.getTokenUri());
		builder.userInfoUri(provider.getUserInfoUri());
		builder.userNameAttributeName(provider.getUserNameAttribute());
		return builder;
	}
	
	// OAuth2ClientPropertiesMapper.getErrorMessage
	private String getErrorMessage(String configuredProviderId, String registrationId) {
		return ((configuredProviderId != null) ? "Unknown provider ID '" + configuredProviderId + "'"
				: "Provider ID must be specified for client registration '" + registrationId + "'");
	}
	
	// OAuth2ClientPropertiesMapper.getCommonProvider
	private CommonOAuth2Provider getCommonProvider(String providerId) {
		try {
			return CommonOAuth2Provider.valueOf(providerId.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

	private OAuth2CustomProvider getCustomProvider(String providerId) {
		try {
			return OAuth2CustomProvider.valueOf(providerId.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}
	
}
