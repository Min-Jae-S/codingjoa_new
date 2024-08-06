package com.codingjoa.obsolete;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({ "unchecked", "unused" })
@Slf4j
public class OAuth2LoginFilter extends OAuth2LoginAuthenticationFilter {
	
	public static final String DEFAULT_FILTER_PROCESSES_URI = "/login/*/callback";
	private static final String DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME =
			HttpSessionOAuth2AuthorizationRequestRepository.class.getName() + ".AUTHORIZATION_REQUEST";
	private final ClientRegistrationRepository clientRegistrationRepository;
	private final OAuth2AuthorizedClientRepository authorizedClientRepository;
	private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository = 
			new HttpSessionOAuth2AuthorizationRequestRepository();
	
	public OAuth2LoginFilter(ClientRegistrationRepository clientRegistrationRepository,
								OAuth2AuthorizedClientService authorizedClientService) {
		super(clientRegistrationRepository, authorizedClientService, DEFAULT_FILTER_PROCESSES_URI);
		this.clientRegistrationRepository = clientRegistrationRepository;
		this.authorizedClientRepository = new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
	}

	public OAuth2LoginFilter(ClientRegistrationRepository clientRegistrationRepository,
								OAuth2AuthorizedClientRepository authorizedClientRepository) {
		super(clientRegistrationRepository, authorizedClientRepository, DEFAULT_FILTER_PROCESSES_URI);
		this.clientRegistrationRepository = clientRegistrationRepository;
		this.authorizedClientRepository = authorizedClientRepository;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("## {}.attemptAuthentication", this.getClass().getSimpleName());
		
		MultiValueMap<String, String> params = toMultiMap(request.getParameterMap());
		log.info("\t > params = {}", params.keySet());
		
		String stateParamter = request.getParameter(OAuth2ParameterNames.STATE);
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
		//log.info("\t > before removing authorizationRequest, exists ? {}", authorizationRequests.containsKey(stateParamter));

		OAuth2AuthorizationRequest authorizationRequest = 
				this.authorizationRequestRepository.removeAuthorizationRequest(request, response);
		//log.info("\t > after removing authorizationRequest, exists ? {}", authorizationRequests.containsKey(stateParamter));
		
		// clientRegistration of OAuth2AuthorizationRequest removed from session 
		String registrationId = authorizationRequest.getAttribute(OAuth2ParameterNames.REGISTRATION_ID);
		ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);
		
		// redirectUri
		String redirectUri = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
				.replaceQuery(null)
				.build()
				.toUriString();
		
		// # generate OAuth2AuthorizationResponse using params(authoriation code, state) and redirectUri
		// OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponseUtils.convert(params, redirectUri);
		
		// # generate OAuth2LoginAuthenticationToken using clientRegistration, authorizationRequest, authorizationResponse
		// OAuth2LoginAuthenticationToken authenticationRequest = new OAuth2LoginAuthenticationToken(
		//		clientRegistration, new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse));
		
		// # authenticate OAuth2LoginAuthenticationToken by OAuth2LoginAuthenticationProvider
		// OAuth2LoginAuthenticationToken authenticationResult =
		//		(OAuth2LoginAuthenticationToken) this.getAuthenticationManager().authenticate(authenticationRequest);
		
		authorizationRequestRepository.saveAuthorizationRequest(authorizationRequest, request, response);
		//log.info("\t > save removed authorizationRequest for test");
		
		log.info("\t > delegate to {} for authentication attempt", this.getClass().getSuperclass().getSimpleName());
		return super.attemptAuthentication(request, response);
	}
	
	// HttpSessionOAuth2AuthorizationRequestRepository, HttpCookieOAuth2AuthorizationRequestRepository
	private Map<String, OAuth2AuthorizationRequest> getAuthorizationRequests(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = session == null ? null :
				(Map<String, OAuth2AuthorizationRequest>) session.getAttribute(DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME);
		if (authorizationRequests == null) {
			return new HashMap<>();
		}
		
		return authorizationRequests;
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

}
