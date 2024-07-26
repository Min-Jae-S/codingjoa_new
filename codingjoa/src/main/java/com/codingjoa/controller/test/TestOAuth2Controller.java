package com.codingjoa.controller.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
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
		List<CommonOAuth2Provider> commonOAuth2Providers = Arrays.stream(CommonOAuth2Provider.values())
				.collect(Collectors.toList());
		List<CustomOAuth2Provider> customOAuth2Providers = Arrays.stream(CustomOAuth2Provider.values())
				.collect(Collectors.toList());
		log.info("\t > commonOAuth2Providers = {}", commonOAuth2Providers);
		log.info("\t > customOAuth2Providers = {}", customOAuth2Providers);
		
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

}
