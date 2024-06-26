package com.codingjoa.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@GetMapping("/login") 
	public String loginPage(@RequestParam(required = false) String redirect, Model model) {
		log.info("## loginPage");
		log.info("\t > redirect = {}", redirect);
		model.addAttribute("redirect", redirect);
		return "login";
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

	@ResponseBody
	@GetMapping("/api/authentication-details-check")
	public ResponseEntity<Object> checkAuthenticationDetails(Authentication authentication) {
		log.info("## checkAuthenticationDetails");
		if (authentication == null) {
			log.info("\t > authentication is null");
		} else {
			Object object = authentication.getDetails();
			if (object == null) {
				log.info("\t > authentication details is null");
			} 
		}
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.message("success")
				.build());
	}
	
	@ResponseBody
	@GetMapping("/api/url")
	public ResponseEntity<Object> validateUrl(@RequestBody Map<String, Object> map, HttpServletRequest request) {
		log.info("## validateUrl");
		
		String url = null;
		for (String key : map.keySet()) {
			url = (String) map.get(key);
		}
		log.info("\t > url = {}", (url == null) ? null : "'" + url + "'");
		log.info("\t > valid url ? {}", isValidUrl(request, url));
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.message("success")
				.build());
	}
	
	private boolean isValidUrl(HttpServletRequest request, String url) {
		// URL format validation, Allowed domain validation
		try {
			URL parsedUrl = new URL(url);
			String protocol = parsedUrl.getProtocol();
			
			// if parsedUrl is not null, then protocol is not null
			if (!protocol.equals("http") && !protocol.equals("https")) {
				log.info("\t > invalid protocol : {}", protocol);
				return false;
			}
			
			String host = parsedUrl.getHost();
			String currentHost = request.getServerName();
			
			if (!host.equals(currentHost)) {
				log.info("\t > invalid host : {}", host);
				return false;
			}
			
			return true;
		} catch (MalformedURLException e) {
			log.info("\t > invalid url format");
			log.info("\t > {} : {}", e.getClass().getSimpleName(), e.getMessage());
			return false;
		}
	}
	
	private String getRedirectURL(HttpServletRequest request, HttpServletResponse response) {
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response); // DefaultSavedRequest 
		return (savedRequest == null) ? request.getContextPath() : savedRequest.getRedirectUrl();
	}
	
}
