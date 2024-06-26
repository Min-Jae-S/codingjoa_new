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
	public String loginPage(@RequestParam(required = false) String redirect, Model model, HttpServletRequest request) {
		log.info("## loginPage");
		log.info("\t > redirect = {}", (redirect == null) ? null : "'" + redirect + "'");
		if (!isValidUrl(request, redirect)) {
			log.info("\t > invalid redirect - default redirect will be set");
			redirect = ServletUriComponentsBuilder.fromContextPath(request)
					.path("/")
					.build()
					.toString();
		} else {
			log.info("\t > valid redirect");
		}
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

	private boolean isValidUrl(HttpServletRequest request, String url) {
		if (!StringUtils.hasText(url)) {
			return false;
		}
		
		StringBuffer requestURL = request.getRequestURL(); 				// http://localhost:8888/codingjoa/**
		String contextPath = request.getContextPath();					// /codingjoa
		
		int contextPathIndex = requestURL.indexOf(contextPath) + contextPath.length();
		String baserUrl = requestURL.substring(0, contextPathIndex);	// http://localhost:8888/codingjoa
		
		return new AntPathMatcher().match(baserUrl + "/**", url);
	}
	
	private String getRedirectURL(HttpServletRequest request, HttpServletResponse response) {
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response); // DefaultSavedRequest 
		return (savedRequest == null) ? request.getContextPath() : savedRequest.getRedirectUrl();
	}
	
}
