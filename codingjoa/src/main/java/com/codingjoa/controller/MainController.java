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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.util.FormatUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {
	
	@GetMapping
	public String main() {
		log.info("## main");
		return "main";
	}

	@GetMapping("/login") 
	public String loginPage(@RequestParam(name = "continue", required = false) String continueUrl, Model model) {
		log.info("## loginPage");
		log.info("\t > continueUrl = {}", FormatUtils.formatString(continueUrl));
		model.addAttribute("continueUrl", continueUrl);
		return "login";
	}
	
	@ResponseBody
	@GetMapping("/api/saved-request")
	public ResponseEntity<Object> getSavedRequest(HttpServletRequest request, HttpServletResponse response) {
		log.info("## getSavedRequest");
		String redirectUrl = getRedirectURL(request, response);
		log.info("\t > redirectUrl = {}", FormatUtils.formatString(redirectUrl));
		
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
