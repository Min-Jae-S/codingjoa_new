package com.codingjoa.controller.test;

import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${security.oauth2.kakao.client-id}")
	private String kakaoClientId;
	
	@Value("${security.oauth2.kakao.redirect-uri}")
	private String kakaoRedirectUri;

	@Value("${security.oauth2.kakao.auth-url}")
	private String kakaoAuthUrl;
	

	@GetMapping("/oauth2")
	public String oAuth2Main(Model model) {
		log.info("## oAuth2 main");
		
		String kakaoLoginUrl = UriComponentsBuilder.fromHttpUrl(kakaoAuthUrl)
				.queryParam("response_type", "code")
				.queryParam("client_id", kakaoClientId)
				.queryParam("redirect_uri", kakaoRedirectUri)
				.build()
				.toString();
		log.info("\t > kakaoLoginUrl = {}", kakaoLoginUrl);
		
		model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);
		return "test/oauth2";
	}
}
