package com.codingjoa.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
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
import org.springframework.web.util.UriUtils;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.util.Utils;
import com.nimbusds.jose.util.StandardCharset;

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
	
	@GetMapping("/login") 
	public String loginPage(@RequestParam(name = "continue", required = false) String continueUrl, Model model) {
		log.info("## loginPage");
		log.info("\t > continue = {}", Utils.formatString(continueUrl));
		String newContinueUrl = resolveContinueUrl(continueUrl);
		model.addAttribute("continueUrl", UriUtils.encode(newContinueUrl, StandardCharset.UTF_8));
		return "login";
	}
	
	private boolean isValidUrl(String url) {
		if (!StringUtils.hasText(url)) {
			return false;
		}
		
		String pattern = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/**")
				.build(false)
				.toUriString();
		return new AntPathMatcher().match(pattern, url);
	}
	
	private String resolveContinueUrl(String continueUrl) {
		if (!isValidUrl(continueUrl)) {
			log.info("\t > missing or invalid continueUrl, setting default continueUrl");
			return ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/")
					.build()
					.toUriString();
		} else {
			log.info("\t > valid continueUrl, setting continueUrl from request");
			return continueUrl;
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
