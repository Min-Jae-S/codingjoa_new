package com.codingjoa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/config")
@RestController
public class ConfigController {
	
	@GetMapping("/filters")
	public ResponseEntity<Object> filters() {
		log.info("## filters called...");
		
		return null;
	}

	@GetMapping("/converters")
	public ResponseEntity<Object> converters() {
		log.info("## converters called...");
		return null;
	}
	
	
	@GetMapping("/message-converters")
	public ResponseEntity<Object> messageConverters() {
		log.info("## messageConverters called...");
		
		return null;
	}
	
	@GetMapping("/exception-resolvers")
	public ResponseEntity<Object> exceptionResolvers() {
		log.info("## exceptionResolvers called...");
		
		return null;
	}

}
