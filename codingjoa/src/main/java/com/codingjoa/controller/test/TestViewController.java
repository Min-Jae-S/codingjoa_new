package com.codingjoa.controller.test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import com.codingjoa.security.api.KakaoApi;
import com.codingjoa.security.api.NaverApi;

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
	
	@Autowired
	private KakaoApi kakaoApi;
	
	@Autowired
	private NaverApi naverApi;

	@GetMapping("/oauth2")
	public String oAuth2Main(Model model) {
		log.info("## oAuth2 main");
		model.addAttribute("kakaoLoginUrl", createKakaoLoginUrl());
		model.addAttribute("naverLoginUrl", createNaverLoginUrl());
		return "test/oauth2";
	}
	
	private String createKakaoLoginUrl() {
		// https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}
		return UriComponentsBuilder.fromHttpUrl(kakaoApi.getAuthorizeUrl())
				.queryParam("response_type", "code")
				.queryParam("client_id", kakaoApi.getClientId())
				.queryParam("redirect_uri", URLEncoder.encode(kakaoApi.getRedirectUri(), StandardCharsets.UTF_8))
				.queryParam("prompt", "login") // re-authenticate the user regardless of previous login status
				.build()
				.toString();
	}
	
	private String createNaverLoginUrl() {
		// https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=CLIENT_ID&state=STATE_STRING&redirect_uri=CALLBACK_URL
		return UriComponentsBuilder.fromHttpUrl(naverApi.getAuthorizeUrl())
				.queryParam("response_type", "code")
				.queryParam("client_id", naverApi.getClientId())
				.queryParam("redirect_uri", URLEncoder.encode(naverApi.getRedirectUri(), StandardCharsets.UTF_8))
				.queryParam("state", "test")
				.build()
				.toString();
	}
}
