package com.codingjoa.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
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
	private ApplicationContext applicationContext; // ConfigurableApplticationContext
	
	@Autowired
	private ServletContext servletContext;
	
	@GetMapping("/filters")
	public ResponseEntity<Object> filters() {
		log.info("## filters called...");
		
		FilterChainProxy filterChainProxy = applicationContext.getBean(FilterChainProxy.class);
		SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(0);
		List<String> filters = securityFilterChain.getFilters()
				.stream()
				.map(filter -> filter.getClass().getName())
				.collect(Collectors.toList());
		filters.forEach(filter -> log.info("\t {}", filter.substring(filter.lastIndexOf(".") + 1)));
		
		return ResponseEntity.ok(SuccessResponse.create().data(filters));
	}

	@GetMapping("/converters")
	public ResponseEntity<Object> converters() {
		log.info("## converters called...");
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	
	@GetMapping("/message-converters")
	public ResponseEntity<Object> messageConverters() {
		log.info("## messageConverters called...");
		
		RequestMappingHandlerAdapter handlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<String> converters = handlerAdapter.getMessageConverters()
				.stream()
				.map(converter -> converter.getClass().getName())
				.collect(Collectors.toList());
		converters.forEach(converter -> log.info("\t {}", converter.substring(converter.lastIndexOf(".") +1)));
		
		return ResponseEntity.ok(SuccessResponse.create().data(converters));
	}
	
	@GetMapping("/exception-resolvers")
	public ResponseEntity<Object> exceptionResolvers() {
		log.info("## exceptionResolvers called...");
		
		HandlerExceptionResolverComposite composite = applicationContext.getBean(HandlerExceptionResolverComposite.class);
		List<String> resolvers = composite.getExceptionResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getName())
				.collect(Collectors.toList());
		resolvers.forEach(resolver -> log.info("\t {}", resolver.substring(resolver.lastIndexOf(".") + 1)));
		
		return ResponseEntity.ok(SuccessResponse.create().data(resolvers));
	}

}
