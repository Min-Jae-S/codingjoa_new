package com.codingjoa.controller.test;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@RestController
public class TestSessionController {
	
	@GetMapping("/session/test1")
	public ResponseEntity<Object> test1(HttpSession session) { 
		log.info("## test1");
		log.info("\t > sessionId = {}", session.getId());
		
		long creationTime = session.getCreationTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		log.info("\t > created at '{}'", sdf.format(creationTime));
		
		List<String> attributeNames = Collections.list(session.getAttributeNames());
		if (attributeNames.size() > 0) {
			log.info("\t > attributesNames = {}", attributeNames);
		} else {
			log.info("\t > no attributes");
		}
		return ResponseEntity.ok("success");
	}

	@GetMapping("/session/test2")
	public ResponseEntity<Object> test2() { 
		log.info("## test2");
		return ResponseEntity.ok("success");
	}

	@GetMapping("/session/test3")
	public ResponseEntity<Object> test3(HttpSession session, Authentication authenticaion) { 
		log.info("## test3");
		
		/* 
		 * 	@@ https://www.inflearn.com/questions/523118/controller%EC%97%90-authentication%ED%83%80%EC%9E%85-%ED%8C%8C%EB%9D%BC%EB%AF%B8%ED%84%B0%EB%A5%BC-%EC%A3%BC%EC%9E%85%ED%95%98%EB%8A%94-handlermethodargumentresolver%EC%9D%98-%EA%B5%AC%ED%98%84%EC%B2%B4%EA%B0%80-%EB%AC%B4%EC%97%87%EC%9D%B8%EA%B0%80
		 *	When the user is authenticated, an Authentication object is injected.
		 * 	However, if the user is anonymous, no Authentication object is injected
		 */
		log.info("\t > authentication from paramter = {}", authenticaion == null ? null : authenticaion.getClass().getSimpleName());
		log.info("\t > authentication from securityContext = {}", 
				SecurityContextHolder.getContext().getAuthentication().getClass().getSimpleName());
		
		Enumeration<String> sessionAttriubteNames = session.getAttributeNames();
		List<String> sessionKeys = Collections.list(sessionAttriubteNames);
		
		for (String sessionKey : sessionKeys) {
			log.info("\t > sessionKey = {}", sessionKey);
		}
		
		/*	
		 * 	Aussme the user is authenticated.
		 *  if SessionCreationPolicy.STATELESS is configured, 
		 *  	SPRING_SECURITY_CONTEXT is stored in the session
		 * 	if SessionCreationPolicy.STATELESS is not conigured, 
		 * 		SPRING_SECURITY_CONTEXT is NOT stored in the session
		 */
		
		boolean matches = sessionKeys.stream().anyMatch(sessionKey -> "SPRING_SECURITY_CONTEXT".equals(sessionKey));
		log.info("\t > {}", (matches == true) ? 
				"SPRING_SECURITY_CONTEXT is stored in the session" : "SPRING_SECURITY_CONTEXT is NOT stored in the session");
		
		return ResponseEntity.ok("success");
	}
	
}
 