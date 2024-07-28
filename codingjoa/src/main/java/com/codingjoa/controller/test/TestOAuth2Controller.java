package com.codingjoa.controller.test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
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
import com.codingjoa.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/oauth2")
@RestController
public class TestOAuth2Controller<B> {
	
	@Autowired
	private OAuth2Service oAuth2Service;
	
	@GetMapping("/kakao/callback")
	public ResponseEntity<Object> kakaoCallback(@RequestParam String code, @RequestParam String state) throws Exception {
		log.info("## kakaoCallback");
		log.info("\t > authorization code = {}", code);
		log.info("\t > state = {}", state);
		
		KakaoTokenResponse kakaoToken = oAuth2Service.getKakaoToken(code, state);
		log.info("\t > request kakaoToken");
		log.info("{}", JsonUtils.formatJson(kakaoToken));
		
		KakaoUserInfoResponse kakaoUserInfo = oAuth2Service.getKakaoUserInfo(kakaoToken.getAccessToken());
		log.info("\t > request kakaoUserInfo");
		log.info("{}", JsonUtils.formatJson(kakaoUserInfo));
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/naver/callback")
	public ResponseEntity<Object> naverCallback(@RequestParam String code, @RequestParam String state) throws Exception {
		log.info("## naverCallback");
		log.info("\t > authorization code = {}", code);
		log.info("\t > state = {}", state);
		
		NaverTokenResponse naverToken = oAuth2Service.getNaverToken(code, state);
		log.info("\t > request naverToken");
		log.info("{}", JsonUtils.formatJson(naverToken));
		
		NaverUserInfoResponse naverUserInfo = oAuth2Service.getNaverUserInfo(naverToken.getAccessToken());
		log.info("\t > request naverUserInfo");
		log.info("{}", JsonUtils.formatJson(naverUserInfo));
		
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
	public ResponseEntity<Object> test3(HttpServletRequest request, HttpServletResponse response, B builder) {
		log.info("## test3");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	

}
