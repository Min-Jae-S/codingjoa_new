package com.codingjoa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

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
		
		DefaultSecurityFilterChain filterChain = context.getBean(DefaultSecurityFilterChain.class);
		List<String> filters = filterChain.getFilters()
				.stream()
				.map(filter -> filter.getClass().getName())
				.collect(Collectors.toList());
		filters.forEach(filter -> log.info("\t {}", filter));
		
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
		
		RequestMappingHandlerAdapter handlerAdapter = context.getBean(RequestMappingHandlerAdapter.class);
		List<String> converters = handlerAdapter.getMessageConverters()
				.stream()
				.map(converter -> converter.getClass().getName())
				.collect(Collectors.toList());
		converters.forEach(converter -> log.info("\t {}", converter));
		
		return ResponseEntity.ok(SuccessResponse.create().data(converters));
	}
	
	@GetMapping("/exception-resolvers")
	public ResponseEntity<Object> exceptionResolvers() {
		log.info("## exceptionResolvers called...");
		
		HandlerExceptionResolverComposite composite = context.getBean(HandlerExceptionResolverComposite.class);
		List<String> resolvers = composite.getExceptionResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getName())
				.collect(Collectors.toList());
		resolvers.forEach(resolver -> log.info("\t {}", resolver));
		
		return ResponseEntity.ok(SuccessResponse.create().data(resolvers));
	}

}
