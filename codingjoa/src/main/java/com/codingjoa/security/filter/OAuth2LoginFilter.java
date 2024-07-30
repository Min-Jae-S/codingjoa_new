package com.codingjoa.security.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

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
//		log.info("\t > state from authorization response = {}", authorizationResponseMap.get("state")[0]);
		
//		Map<String, OAuth2AuthorizationRequest> authorizationRequests = getAuthorizationRequests(request);
//		if (!authorizationRequests.isEmpty()) {
//			authorizationRequests.forEach((key, value) -> {
//				log.info("\t > state from session = {}", key);
//			});
//		} else {
//			log.info("\t > no authorizationRequests in the session");
//		}
		
		log.info("\t > delegating authentication attempt to {}", this.getClass().getSuperclass().getSimpleName());
		return super.attemptAuthentication(request, response);
	}
	
	// HttpSessionOAuth2AuthorizationRequestRepository, HttpCookieOAuth2AuthorizationRequestRepository
	@SuppressWarnings({ "unchecked", "unused" })
	private Map<String, OAuth2AuthorizationRequest> getAuthorizationRequests(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String authorizationRequestAttrName = 
				HttpSessionOAuth2AuthorizationRequestRepository.class.getName() +  ".AUTHORIZATION_REQUEST";
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = session == null ? null :
				(Map<String, OAuth2AuthorizationRequest>) session.getAttribute(authorizationRequestAttrName);
		if (authorizationRequests == null) {
			return new HashMap<>();
		}
		
		return authorizationRequests;
	}

}
