package com.codingjoa.controller.test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.util.FormatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/test/sse")
public class TestSseController {

	private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	private final ObjectMapper objectMapper;
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authenticationName = authentication.getClass().getSimpleName();
		log.info("\t > authentication = {}", authenticationName);
		
		Object principal = null;
		if (authentication != null) {
			principal = authentication.getPrincipal();
			String json = FormatUtils.formatPrettyJson(principal);
			log.info("\t > principal: {}", json != null ? json : principal);
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().data(principal).build());
	}
	
	@GetMapping("/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		SuccessResponse response = SuccessResponse.builder().build();
		log.info("\t > response: {}", FormatUtils.formatPrettyJson(response));
		
		return ResponseEntity.ok(response);
	}

	@GetMapping("/stream")
	public SseEmitter stream() {
		SseEmitter emitter = new SseEmitter();
		log.info("\t > emitter timeout = {}", emitter.getTimeout());
		
		return emitter;
	}
	
	@GetMapping("/subscribe")
	public SseEmitter subscribe() {
		SseEmitter emitter = new SseEmitter();
		emitters.add(emitter);
		
		return emitter;
	}

	@PostMapping("/broadcast")
	public void broadcast(@RequestParam String message) throws IOException {
		for (SseEmitter emitter : emitters) {
			log.info("\t > {}", emitter);
			emitter.send(message);
		}
	}
}
