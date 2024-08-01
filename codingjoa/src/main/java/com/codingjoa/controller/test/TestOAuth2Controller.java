package com.codingjoa.controller.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.KakaoTokenResponse;
import com.codingjoa.security.dto.KakaoUserInfoResponse;
import com.codingjoa.security.dto.NaverTokenResponse;
import com.codingjoa.security.dto.NaverUserInfoResponse;
import com.codingjoa.security.oauth2.CustomOAuth2Provider;
import com.codingjoa.security.service.TestOAuth2Service;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({ "unused", "unchecked" })
@Slf4j
@RequestMapping("/test/oauth2")
@RestController
public class TestOAuth2Controller {
	
	@Autowired
	private TestOAuth2Service testOAuth2Service;
	
	@GetMapping("/kakao/callback")
	public ResponseEntity<Object> kakaoCallback(@RequestParam String code, @RequestParam String state) throws Exception {
		log.info("## kakaoCallback");
		log.info("\t > authorization code = {}", code);
		log.info("\t > state = {}", state);
		
		log.info("\t > request kakaoToken");
		KakaoTokenResponse kakaoToken = testOAuth2Service.getKakaoToken(code, state);
		
		log.info("\t > request kakaoUserInfo");
		KakaoUserInfoResponse kakaoUserInfo = testOAuth2Service.getKakaoUserInfo(kakaoToken.getAccessToken());
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/naver/callback")
	public ResponseEntity<Object> naverCallback(@RequestParam String code, @RequestParam String state) throws Exception {
		log.info("## naverCallback");
		log.info("\t > authorization code = {}", code);
		log.info("\t > state = {}", state);
		
		log.info("\t > request naverToken");
		NaverTokenResponse naverToken = testOAuth2Service.getNaverToken(code, state);
		
		log.info("\t > request naverUserInfo");
		NaverUserInfoResponse naverUserInfo = testOAuth2Service.getNaverUserInfo(naverToken.getAccessToken());
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	//	URI uri = UriComponentsBuilder.fromHttpUrl(kakaoAccessTokenUrl)
	//		.queryParam("grant_type", "authorization_code")
	//		.queryParam("client_id", kakaoClientId)
	//		.queryParam("redirect_uri", kakaoRedirectUri)
	//		.queryParam("code", code)
	//		.build().toUri();
	
	//	Flux<KakaoTokenResponseDto> response = webClient.post()
	//		.uri(uri)
	//		.contentType(MediaType.APPLICATION_JSON)
	//		.retrieve()
	//		.bodyToFlux(KakaoTokenResponseDto.class);
	
	
	@Qualifier("testClientRegistrationRepository")
	@Autowired
	private InMemoryClientRegistrationRepository clientRegistrationRepository;
	
	private static final String DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME = 
			HttpSessionOAuth2AuthorizationRequestRepository.class.getName() + ".AUTHORIZATION_REQUEST";
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test1(HttpServletRequest request, HttpServletResponse response) {
		log.info("## test1");
		
		ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("kakao");
		DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
				clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
		OAuth2AuthorizationRequest authorizationRequest = resolver.resolve(request, clientRegistration.getRegistrationId());
		
		log.info("\t > redirectUri = {}", authorizationRequest.getRedirectUri());
		log.info("\t > authorizationRequestUri = {}", authorizationRequest.getAuthorizationRequestUri());
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		List<Object> oAuth2Providers = Stream
				.concat(Arrays.stream(CommonOAuth2Provider.values()), Arrays.stream(CustomOAuth2Provider.values()))
				.collect(Collectors.toList());
		log.info("\t > oAuth2Providers = {}", oAuth2Providers);
		
		CustomOAuth2Provider kakaoOAuth2Provider;
		CustomOAuth2Provider KAKAOAuth2Provider;
		try {
			kakaoOAuth2Provider = CustomOAuth2Provider.valueOf("kakao");
		} catch (Exception e) {
			kakaoOAuth2Provider = null;
		}
		
		try {
			KAKAOAuth2Provider = CustomOAuth2Provider.valueOf("KAKAO");
		} catch (Exception e) {
			KAKAOAuth2Provider = null;
		}
		
		log.info("\t > kakaoOAuth2Provider = {}", kakaoOAuth2Provider);
		log.info("\t > KAKAOAuth2Provider = {}", KAKAOAuth2Provider);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/test3")
	public ResponseEntity<Object> test3(HttpServletRequest request) {
		log.info("## test3");
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = getAuthorizationRequests(request);
		log.info("\t > authorizationRequests = {}", authorizationRequests == null ? "null" : authorizationRequests.keySet());
		
		if (!authorizationRequests.isEmpty()) {
			authorizationRequests.forEach((key, value) -> {
				log.info("\t > state from session = {}", key);
			});
		} else {
			log.info("\t > no authorizationRequests in the session");
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	private Map<String, OAuth2AuthorizationRequest> getAuthorizationRequests(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Map<String, OAuth2AuthorizationRequest> authorizationRequests = session == null ? null :
				(Map<String, OAuth2AuthorizationRequest>) session.getAttribute(DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME);
		if (authorizationRequests == null) {
			return new HashMap<>();
		}
		
		return authorizationRequests;
	}
	
	@Autowired(required = false)
	private AuthenticationManager authenticationManager;
	
	@Autowired(required = false)
	private ProviderManager providerManager;

	@GetMapping("/test4")
	public ResponseEntity<Object> test4() {
		log.info("## test4");
		log.info("\t > authenticationManager = {}", authenticationManager);
		log.info("\t > providerManager = {}", providerManager);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

}
