package com.codingjoa.controller.test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

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
	
	@Qualifier("testClientRegistrationRepository")
	@Autowired
	private InMemoryClientRegistrationRepository clientRegistrationRepository;

	@GetMapping("/oauth2")
	public String oAuth2Main(Model model, HttpServletRequest request) {
		log.info("## oAuth2 main");
		
		clientRegistrationRepository.forEach(clientRegistration -> {
			String attributeName = clientRegistration.getRegistrationId() + "LoginUrl"; // kakaoLoginUrl, naverLoginUrl
			String authorizationRequestUri = buildAuthorizationRequestUri(clientRegistration);
			model.addAttribute(attributeName, authorizationRequestUri);
		});

		return "test/oauth2";
	}
	
	private String buildAuthorizationRequestUri(ClientRegistration clientRegistration) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.set("response_type", encode(OAuth2AuthorizationResponseType.CODE.getValue()));
		params.set("client_id", encode(clientRegistration.getClientId()));
		params.set("redirect_uri", encode(clientRegistration.getRedirectUriTemplate()));
		params.set("state", encode(generateState()));
		
//		if (clientRegistration.getRegistrationId().equals("naver")) {
//			String state = generateState();
//			params.set("state", encode(state));
//		}

		return UriComponentsBuilder.fromHttpUrl(clientRegistration.getProviderDetails().getAuthorizationUri())
				.queryParams(params)
				.build()
				.toUriString();
	}
	
	private String generateState() {
		StringKeyGenerator stateGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder());
		return stateGenerator.generateKey();
	}
	
	private String encode(String value) {
		return UriUtils.encode(value, StandardCharsets.UTF_8);
	}
	
	@GetMapping("/login")
	public String loginMain() {
		log.info("## loginMain main");
		return "test/login";
	}

	@GetMapping("/properties")
	public String propertiesMain() {
		log.info("## loginMain main");
		return "test/properties";
	}
	
	@GetMapping("/object-mapper")
	public String objectMapper() {
		log.info("## objectMapper main");
		return "test/object-mapper";
	}

	@GetMapping("/cookie")
	public String cookie() {
		log.info("## cookie main");
		return "test/cookie";
	}
	
}
