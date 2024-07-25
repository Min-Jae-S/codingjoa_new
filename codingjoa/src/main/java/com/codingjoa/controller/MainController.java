package com.codingjoa.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Controller
public class MainController {
	
	@GetMapping(value = "/")
	public String home() {
		log.info("## home");
		return "home";
	}

	@GetMapping("/accessDenied")
	public String accessDenied() {
		log.info("## accessDenied");
		return "access-denied";
	}
	
	@Qualifier("mainClientRegistrationRepository")
	@Autowired
	private InMemoryClientRegistrationRepository clientRegistrationRepository;
	
	@GetMapping("/login") 
	public String loginPage(@RequestParam(name = "redirect", required = false) String redirectUrl, Model model, HttpServletRequest request) {
		log.info("## loginPage");
		log.info("\t > redirect = {}", (redirectUrl == null) ? null : "'" + redirectUrl + "'");
		model.addAttribute("redirectUrl", resolveRedirectUrl(redirectUrl));
		
		DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver = 
				new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/login");
		clientRegistrationRepository.forEach(clinetRegistration -> {
			OAuth2AuthorizationRequest authorizationRequest = authorizationRequestResolver.resolve(request, clinetRegistration.getRegistrationId());
			// URI string is encoded in the application/x-www-form-urlencoded MIME format.
			log.info("\t > authorizationRequestUri = {} ", authorizationRequest.getAuthorizationRequestUri());
		});
		
		return "login";
	}
	
	private boolean isValidUrl(String url) {
		if (!StringUtils.hasText(url)) {
			return false;
		}
		
		String pattern = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/**")
				.build()
				.toString();
		return new AntPathMatcher().match(pattern, url);
	}
	
	private String resolveRedirectUrl(String redirect) {
		if (!isValidUrl(redirect)) {
			log.info("\t > missing or invalid redirect, setting default redirect");
			return ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/")
					.build()
					.toString();
		} else {
			log.info("\t > valid redirect, setting redirect from request");
			return redirect;
		}
	}
	
	@ResponseBody
	@GetMapping("/api/saved-request")
	public ResponseEntity<Object> getSavedRequest(HttpServletRequest request, HttpServletResponse response) {
		log.info("## getSavedRequest");
		String redirectUrl = getRedirectURL(request, response);
		log.info("\t > redirectUrl = {}", redirectUrl);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.message("success")
				.data(Map.of("redirectUrl", redirectUrl))
				.build());
	}
	
	private String getRedirectURL(HttpServletRequest request, HttpServletResponse response) {
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response); // DefaultSavedRequest 
		return (savedRequest == null) ? request.getContextPath() : savedRequest.getRedirectUrl();
	}
	
}
