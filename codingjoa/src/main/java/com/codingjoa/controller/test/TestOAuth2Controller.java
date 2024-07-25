package com.codingjoa.controller.test;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.KakaoTokenResponse;
import com.codingjoa.security.dto.KakaoUserInfoResponse;
import com.codingjoa.security.dto.NaverTokenResponse;
import com.codingjoa.security.dto.NaverUserInfoResponse;
import com.codingjoa.security.service.OAuth2Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/oauth2")
@RestController
public class TestOAuth2Controller {
	
	@Autowired
	private OAuth2Service oAuth2Service;
	
	@GetMapping("/kakao/callback")
	public ResponseEntity<Object> kakaoCallback(@RequestParam String code) throws Exception {
		log.info("## kakaoCallback");
		log.info("\t > authorization code = {}", code);
		
		KakaoTokenResponse kakaoToken = oAuth2Service.getKakaoToken(code);
		log.info("\t > request kakaoToken ( https://kauth.kakao.com/oauth/token )");
		log.info("\t > {}", kakaoToken);
		
		KakaoUserInfoResponse kakaoUserInfo = oAuth2Service.getKakaoUserInfo(kakaoToken.getAccessToken());
		log.info("\t > request kakaoUserInfo ( https://kapi.kakao.com/v2/user/me )");
		log.info("\t > {}", kakaoUserInfo);
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/naver/callback")
	public ResponseEntity<Object> naverCallback(@RequestParam String code, @RequestParam String state) throws Exception {
		log.info("## naverCallback");
		log.info("\t > authorization code = {}, state = {}", code, state);
		
		NaverTokenResponse naverToken = oAuth2Service.getNaverToken(code, state);
		log.info("\t > request naverToken ( https://nid.naver.com/oauth2.0/token )");
		log.info("\t > {}", naverToken);
		
		NaverUserInfoResponse naverUserInfo = oAuth2Service.getNaverUserInfo(naverToken.getAccessToken());
		log.info("\t > request naverUserInfo ( https://openapi.naver.com/v1/nid/me )");
		log.info("\t > {}", naverUserInfo);
		
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
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test(HttpServletRequest request, HttpServletResponse response) {
		log.info("## test1");
		
		ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("kakao");
		DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
		OAuth2AuthorizationRequest authorizationRequest = resolver.resolve(request, clientRegistration.getRegistrationId());
		
		String redirectUri = authorizationRequest.getRedirectUri();
		String authorizationRequestUri = authorizationRequest.getAuthorizationRequestUri();;
		log.info("\t > redirectUri = {}", redirectUri);
		log.info("\t > encode redirectUri = {}", encodeQueryParam(redirectUri));
		
		log.info("\t > authorizationRequestUri = {}", authorizationRequestUri);
		log.info("\t > encode authorizationRequestUri = {}", encodeQueryParam(authorizationRequestUri));
		
		String uri = "https://kauth.kakao.com/oauth/authorize?response_type=#$#@#";
		log.info("\t > uri = {}", uri);
		log.info("\t > encode uri = {}", encodeQueryParam(uri));
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	private static String encodeQueryParam(String value) {
		return UriUtils.encodeQueryParam(value, StandardCharsets.UTF_8);
	}
	
	@SuppressWarnings("unused")
	private String buildAuthorizationRequestUri(ClientRegistration clientRegistration) {
		// https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}
		log.info("\t > redirectUriTemplate = {}", clientRegistration.getRedirectUriTemplate());
		return UriComponentsBuilder.fromHttpUrl(clientRegistration.getProviderDetails().getAuthorizationUri())
				.queryParam("response_type", OAuth2AuthorizationResponseType.CODE.getValue())
				.queryParam("client_id", clientRegistration.getClientId())
				.queryParam("redirect_uri", clientRegistration.getRedirectUriTemplate())
				.toUriString();
	}

}
