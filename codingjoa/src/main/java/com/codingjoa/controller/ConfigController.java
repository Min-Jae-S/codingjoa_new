package com.codingjoa.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.codingjoa.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/config")
@RestController
public class ConfigController {
	
	@Autowired // WebApplicationContext = ApplicationContext + getServletContext()
	private WebApplicationContext applicationCtx; 
	
	@GetMapping("/filters")
	public ResponseEntity<Object> filters() {
		log.info("## filters called...");
		
		FilterChainProxy filterChainProxy = applicationCtx.getBean(FilterChainProxy.class);
		DelegatingFilterProxy filterProxy = applicationCtx.getBean(DelegatingFilterProxy.class);
		
		SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(0);
		List<String> filters = securityFilterChain.getFilters()
				.stream()
				.map(filter -> filter.getClass().getName())
				.collect(Collectors.toList());
		
		ServletContext servletCtx = applicationCtx.getServletContext();
		FilterRegistration registration = servletCtx.getFilterRegistrations().get("securityFilterChain");
		
		filters.forEach(filter -> log.info("\t > {}", filter.substring(filter.lastIndexOf(".") + 1)));
		
		return ResponseEntity.ok(SuccessResponse.create().data(filters));
	}

	@GetMapping("/message-converters")
	public ResponseEntity<Object> messageConverters() {
		log.info("## messageConverters called...");
		
		RequestMappingHandlerAdapter handlerAdapter = applicationCtx.getBean(RequestMappingHandlerAdapter.class);
		List<String> converters = handlerAdapter.getMessageConverters()
				.stream()
				.map(converter -> converter.getClass().getName())
				.collect(Collectors.toList());
		converters.forEach(converter -> log.info("\t > {}", converter.substring(converter.lastIndexOf(".") +1)));
		
		return ResponseEntity.ok(SuccessResponse.create().data(converters));
	}
	
	@GetMapping("/exception-resolvers")
	public ResponseEntity<Object> exceptionResolvers() {
		log.info("## exceptionResolvers called...");
		
		HandlerExceptionResolverComposite composite = applicationCtx.getBean(HandlerExceptionResolverComposite.class);
		List<String> resolvers = composite.getExceptionResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getName())
				.collect(Collectors.toList());
		resolvers.forEach(resolver -> log.info("\t > {}", resolver.substring(resolver.lastIndexOf(".") + 1)));
		
		return ResponseEntity.ok(SuccessResponse.create().data(resolvers));
	}
	
	@GetMapping("/argument-resolvers")
	public ResponseEntity<Object> argumentResolvers() {
		log.info("## argumentResolvers called...");
		
		RequestMappingHandlerAdapter handlerAdapter = applicationCtx.getBean(RequestMappingHandlerAdapter.class);
		List<String> resolvers = handlerAdapter.getArgumentResolvers()
				.stream()
				.map(resolver -> resolver .getClass().getName())
				.collect(Collectors.toList());
		resolvers.forEach(resolver -> log.info("\t > {}", resolver.substring(resolver.lastIndexOf(".") +1)));
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

}
