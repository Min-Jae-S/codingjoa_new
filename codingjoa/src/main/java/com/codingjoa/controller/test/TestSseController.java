package com.codingjoa.controller.test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/test/sse")
public class TestSseController {

	private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	
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
