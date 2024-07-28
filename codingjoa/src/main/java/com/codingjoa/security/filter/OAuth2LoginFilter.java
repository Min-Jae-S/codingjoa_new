package com.codingjoa.security.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2LoginFilter extends OAuth2LoginAuthenticationFilter {
	
	public static final String DEFAULT_FILTER_PROCESSES_URI = "/login/*/callback";

	public OAuth2LoginFilter(ClientRegistrationRepository clientRegistrationRepository,
								OAuth2AuthorizedClientService authorizedClientService) {
		super(clientRegistrationRepository, authorizedClientService, DEFAULT_FILTER_PROCESSES_URI);
	}
	
	public OAuth2LoginFilter(ClientRegistrationRepository clientRegistrationRepository,
								OAuth2AuthorizedClientRepository authorizedClientRepository, 
								String filterProcessesUrl) {
		super(clientRegistrationRepository, authorizedClientRepository, filterProcessesUrl);
	}

	public OAuth2LoginFilter(ClientRegistrationRepository clientRegistrationRepository,
								OAuth2AuthorizedClientService authorizedClientService, 
								String filterProcessesUrl) {
		super(clientRegistrationRepository, authorizedClientService, filterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("## {}.attemptAuthentication", this.getClass().getSimpleName());
		
		Map<String, String[]> authorizationResponseMap = request.getParameterMap();
		log.info("\t > authorizationResponse = {}", authorizationResponseMap.keySet());
		
		return super.attemptAuthentication(request, response);
	}

}
