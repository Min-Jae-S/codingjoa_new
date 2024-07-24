package com.codingjoa.controller.test;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestViewController {

	@GetMapping("/batch")
	public String batchMain() {
		log.info("## batch main");
		return "test/batch";
	}
	
	@GetMapping("/quartz")
	public String quartzMain() {
		log.info("## quartz main");
		return "test/quartz";
	}
	
	@GetMapping("/scheduler")
	public String schedulerMain() {
		log.info("## scheduler main");
		return "test/scheduler";
	}
	
	@GetMapping("/tx")
	public String txMain() {
		log.info("## tx main");
		return "test/tx";
	}
	
	@GetMapping("/tx-props")
	public String txPropsMain() {
		log.info("## txProps main");
		return "test/tx-props";
	}

	@GetMapping("/jdbc")
	public String jdbcMain() {
		log.info("## jdbc main");
		return "test/jdbc";
	}

	@GetMapping("/jdbc-tx")
	public String jdbcTxMain() {
		log.info("## jdbcTx main");
		return "test/jdbc-tx";
	}

	@GetMapping("/rest-api")
	public String restApiMain() {
		log.info("## restApi main");
		return "test/rest-api";
	}

	@GetMapping("/session")
	public String sessionMain() {
		log.info("## session main");
		return "test/session";
	}
	
	@GetMapping("/redirect")
	public String redirectMain() {
		log.info("## redirect main");
		return "test/redirect";
	}

	@GetMapping("/jwt")
	public String jwtMain() {
		log.info("## jwt main");
		return "test/jwt";
	}

	@GetMapping("/cookie-session")
	public String cookieSessionMain() {
		log.info("## cookieSession main");
		return "test/cookie-session";
	}
	
	@Qualifier("subClientRegistrationRepository")
	@Autowired
	private InMemoryClientRegistrationRepository clientRegistrationRepository;
	
	@GetMapping("/oauth2")
	public String oAuth2Main(Model model) {
		log.info("## oAuth2 main");
		String kakaoLoginUrl = buildKakaoLoginUrl();
		String naverLoginUrl = buildNaverLoginUrl();
		log.info("\t > kakaoLoginUrl = {}", kakaoLoginUrl);
		log.info("\t > naverLoginUrl = {}", naverLoginUrl);
		
		model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);
		model.addAttribute("naverLoginUrl", naverLoginUrl);
		return "test/oauth2";
	}

	private String buildKakaoLoginUrl() {
		// https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}
		ClientRegistration kakaoRegistration = clientRegistrationRepository.findByRegistrationId("kakao");
		String authorizationUri = kakaoRegistration.getProviderDetails().getAuthorizationUri();
		return UriComponentsBuilder.fromHttpUrl(authorizationUri)
				.queryParam("respnse_type", OAuth2AuthorizationResponseType.CODE.getValue())
				.queryParam("client_id", kakaoRegistration.getClientId())
				.queryParam("redirect_uri", kakaoRegistration.getRedirectUriTemplate())
				.encode(StandardCharsets.UTF_8)
				.toUriString();
		
	}
	
	private String buildNaverLoginUrl() {
		// https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=CLIENT_ID&state=STATE_STRING&redirect_uri=CALLBACK_URL
		ClientRegistration naverRegistration = clientRegistrationRepository.findByRegistrationId("naver");
		String authorizationUri = naverRegistration.getProviderDetails().getAuthorizationUri();
		return UriComponentsBuilder.fromHttpUrl(authorizationUri)
				.queryParam("respnse_type", OAuth2AuthorizationResponseType.CODE.getValue())
				.queryParam("client_id", naverRegistration.getClientId())
				.queryParam("redirect_uri", naverRegistration.getRedirectUriTemplate())
				.queryParam("state", "STATE_STRING")
				.encode(StandardCharsets.UTF_8)
				.toUriString();
	}
	
}
