package com.codingjoa.controller.test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
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
		log.info("## test1");
		return ResponseEntity.ok("success");
	}
	
}
