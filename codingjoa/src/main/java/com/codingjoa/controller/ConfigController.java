package com.codingjoa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;

import com.codingjoa.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/config")
@RestController
public class ConfigController {
	
	@Autowired
	private ConfigurableApplicationContext context;
	
	@GetMapping("/filters")
	public ResponseEntity<Object> filters() {
		log.info("## filters called...");
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/converters")
	public ResponseEntity<Object> converters() {
		log.info("## converters called...");
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	
	@GetMapping("/message-converters")
	public ResponseEntity<Object> messageConverters() {
		log.info("## messageConverters called...");
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/exception-resolvers")
	public ResponseEntity<Object> exceptionResolvers() {
		log.info("## exceptionResolvers called...");
		
		HandlerExceptionResolverComposite composite = context.getBean(HandlerExceptionResolverComposite.class);
		List<String> resolvers = composite.getExceptionResolvers().stream()
				.map(r -> r.getClass().getSimpleName())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(SuccessResponse.create().data(resolvers));
	}

}
