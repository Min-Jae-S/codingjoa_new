package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.util.FormatUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MainRestController {
	
	@GetMapping("/saved-request")
	public ResponseEntity<Object> getSavedRequest(HttpServletRequest request, HttpServletResponse response) {
		log.info("## getSavedRequest");
		
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response); // DefaultSavedRequest 
		log.info("\t > savedRequest = {}", savedRequest);
		
		String redirectUrl = (savedRequest == null) ? request.getContextPath() : savedRequest.getRedirectUrl();
		log.info("\t > redirectUrl = {}", FormatUtils.formatString(redirectUrl));
		
		return ResponseEntity.ok(SuccessResponse.builder().data(redirectUrl).build());
	}

}
