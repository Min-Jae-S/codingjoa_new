package com.codingjoa.security.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.codingjoa.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2LoginFilter extends AbstractAuthenticationProcessingFilter { // OAuth2LoginAuthenticationFilter
	
	public static final String DEFAULT_FILTER_PROCESSES_URI = "/login/*/callback";
	private final ClientRegistrationRepository clientRegistrationRepository;
	private final OAuth2AuthorizedClientRepository authorizedClientRepository;
	private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository = 
			new HttpSessionOAuth2AuthorizationRequestRepository();
	
	public OAuth2LoginFilter(ClientRegistrationRepository clientRegistrationRepository,
								OAuth2AuthorizedClientRepository authorizedClientRepository) {
		super(DEFAULT_FILTER_PROCESSES_URI);
		this.clientRegistrationRepository = clientRegistrationRepository;
		this.authorizedClientRepository = authorizedClientRepository;
	}

	public OAuth2LoginFilter(ClientRegistrationRepository clientRegistrationRepository,
								OAuth2AuthorizedClientRepository authorizedClientRepository,
								String filterProcessesUrl) {
		super(filterProcessesUrl);
		this.clientRegistrationRepository = clientRegistrationRepository;
		this.authorizedClientRepository = authorizedClientRepository;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("## {}.attemptAuthentication", this.getClass().getSimpleName());
		
		MultiValueMap<String, String> params = toMultiMap(request.getParameterMap());
		log.info("\t > received authorization response, params = {}", params.keySet());
		
		OAuth2AuthorizationRequest authorizationRequest = 
				authorizationRequestRepository.removeAuthorizationRequest(request, response);
		log.info("\t > remove authorizationRequest from the session");
		
		if (authorizationRequest == null) {
			OAuth2Error oAuth2Error = new OAuth2Error("");
			throw new OAuth2AuthenticationException(oAuth2Error);
		}
		
		String registrationId = authorizationRequest.getAttribute(OAuth2ParameterNames.REGISTRATION_ID);
		ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
		if (clientRegistration == null) {
			OAuth2Error oAuth2Error = new OAuth2Error("");
			throw new OAuth2AuthenticationException(oAuth2Error);
		}
		
		String redirectUri = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
				.replaceQuery(null)
				.build()
				.toUriString();
		
		OAuth2AuthorizationResponse authorizationResponse = convert(params, redirectUri);
		OAuth2AuthorizationExchange authorizationExchange = new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse);
		OAuth2LoginAuthenticationToken loginToken = new OAuth2LoginAuthenticationToken(clientRegistration, authorizationExchange);
		
		// authenticate OAuth2LoginAuthenticationToken by OAuth2LoginProvider (request for accessToken and userInfo)
		OAuth2LoginAuthenticationToken authenticatedLoginToken = 
				(OAuth2LoginAuthenticationToken) this.getAuthenticationManager().authenticate(loginToken);
		
		OAuth2AuthenticationToken oAuth2AuthenticationToken = new OAuth2AuthenticationToken(
				authenticatedLoginToken.getPrincipal(),
				authenticatedLoginToken.getAuthorities(),
				authenticatedLoginToken.getClientRegistration().getRegistrationId());

		String continueUrl = authorizationRequest.getAttribute("continue");
		oAuth2AuthenticationToken.setDetails(continueUrl);
		log.info("## set the continueUrl in details : {}", Utils.formatString(continueUrl));

		OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
				authenticatedLoginToken.getClientRegistration(),
				authenticatedLoginToken.getName(),
				authenticatedLoginToken.getAccessToken(),
				authenticatedLoginToken.getRefreshToken());

		authorizedClientRepository.saveAuthorizedClient(authorizedClient, oAuth2AuthenticationToken, request, response);
		
		return oAuth2AuthenticationToken;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		//SecurityContextHolder.getContext().setAuthentication(authResult)
		super.successfulAuthentication(request, response, chain, authResult); 
	}

	static MultiValueMap<String, String> toMultiMap(Map<String, String[]> map) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>(map.size());
		map.forEach((key, values) -> {
			if (values.length > 0) {
				for (String value : values) {
					params.add(key, value);
				}
			}
		});
		
		return params;
	}
	
	static OAuth2AuthorizationResponse convert(MultiValueMap<String, String> params, String redirectUri) {
		String code = params.getFirst(OAuth2ParameterNames.CODE);
		String errorCode = params.getFirst(OAuth2ParameterNames.ERROR);
		String state = params.getFirst(OAuth2ParameterNames.STATE);

		
		if (StringUtils.hasText(code)) {
			return OAuth2AuthorizationResponse.success(code)
					.redirectUri(redirectUri)
					.state(state)
					.build();
		} else {
			String errorDescription = params.getFirst(OAuth2ParameterNames.ERROR_DESCRIPTION);
			String errorUri = params.getFirst(OAuth2ParameterNames.ERROR_URI);
			return OAuth2AuthorizationResponse.error(errorCode)
					.redirectUri(redirectUri)
					.errorDescription(errorDescription)
					.errorUri(errorUri)
					.state(state)
					.build();
		}
	}

}
