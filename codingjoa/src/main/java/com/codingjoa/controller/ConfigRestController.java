package com.codingjoa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterRegistration;
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
public class ConfigRestController {
	
	@Autowired // WebApplicationContext = ApplicationContext + getServletContext()
	private WebApplicationContext webApplicationContext;

	@GetMapping("/filters")
	public ResponseEntity<Object> filters() {
		log.info("## filters called...");
		
		ArrayList<String> filterList = new ArrayList<>();
		
		FilterChainProxy filterChainProxy = webApplicationContext.getBean(FilterChainProxy.class);
		List<SecurityFilterChain> securityFilterChains = filterChainProxy.getFilterChains();
		for (SecurityFilterChain securityFilterChain : securityFilterChains) {
			if (securityFilterChain instanceof DelegatingFilterProxy) {
				securityFilterChain.getFilters().forEach(filter -> {
					filterList.add(filter.getClass().getSimpleName());
				});
			}
		}
		
//		SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(0);
//		List<String> filters = securityFilterChain.getFilters()
//				.stream()
//				.map(filter -> filter.getClass().getSimpleName())
//				.collect(Collectors.toList());
		
		ServletContext servletContext = webApplicationContext.getServletContext();
		Map<String, ? extends FilterRegistration> map = servletContext.getFilterRegistrations();
		for (String filterName : map.keySet()) {
			if (!"springSecurityFilterChain".equals(filterName)) {
				filterList.add(filterName);
			}
		}
		
		filterList.forEach(filter -> {
			int beginIndex = filter.lastIndexOf(".") + 1;
			log.info("\t > {}", filter.substring(beginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(filterList));
	}

	@GetMapping("/message-converters")
	public ResponseEntity<Object> messageConverters() {
		log.info("## messageConverters called...");

		RequestMappingHandlerAdapter handlerAdapter = webApplicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<String> converterList = handlerAdapter.getMessageConverters()
				.stream()
				.map(converter -> converter.getClass().getSimpleName())
				.collect(Collectors.toList());
		
		converterList.forEach(converter -> {
			int beginIndex = converter.lastIndexOf(".") + 1;
			log.info("\t > {}", converter.substring(beginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(converterList));
	}
	
	@GetMapping("/exception-resolvers")
	public ResponseEntity<Object> exceptionResolvers() {
		log.info("## exceptionResolvers called...");
		
		HandlerExceptionResolverComposite composite = 
				webApplicationContext.getBean(HandlerExceptionResolverComposite.class);
		List<String> resolverList = composite.getExceptionResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getSimpleName())
				.collect(Collectors.toList());
		
		resolverList.forEach(resolver -> {
			int beginIndex = resolver.lastIndexOf(".") + 1;
			log.info("\t > {}", resolver.substring(beginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(resolverList));
	}
	
	@GetMapping("/argument-resolvers")
	public ResponseEntity<Object> argumentResolvers() {
		log.info("## argumentResolvers called...");
		
		RequestMappingHandlerAdapter handlerAdapter = webApplicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<String> resolverList = handlerAdapter.getArgumentResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getSimpleName())
				.collect(Collectors.toList());
		
		resolverList.forEach(resolver -> {
			int beginIndex = resolver.lastIndexOf(".") + 1;
			log.info("\t > {}", resolver.substring(beginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(resolverList));
	}

}
