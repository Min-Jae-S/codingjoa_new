package com.codingjoa.controller;

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
		FilterChainProxy filterChainProxy = webApplicationContext.getBean(FilterChainProxy.class);
		List<String> filters = filterChainProxy.getFilterChains()
			.stream()
			.filter(filterChain -> filterChain instanceof SecurityFilterChain)
			.findFirst().get().getFilters()
			.stream()
			.map(filter -> filter.getClass().getSimpleName())
			.collect(Collectors.toList());
		
//		SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(0);
//		List<String> filters = securityFilterChain.getFilters()
//				.stream()
//				.map(filter -> filter.getClass().getSimpleName())
//				.collect(Collectors.toList());
		
		ServletContext servletContext = webApplicationContext.getServletContext();
		Map<String, ? extends FilterRegistration> map = servletContext.getFilterRegistrations();
		for (String filterName : map.keySet()) {
			if (!"springSecurityFilterChain".equals(filterName)) {
				filters.add(filterName);
			}
		}
		
		filters.forEach(filter -> {
			log.info("\t > {}", filter.substring(filter.lastIndexOf(".") + 1));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(filters));
	}

	@GetMapping("/message-converters")
	public ResponseEntity<Object> messageConverters() {
		RequestMappingHandlerAdapter handlerAdapter = webApplicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<String> messageConverters = handlerAdapter.getMessageConverters()
				.stream()
				.map(converter -> converter.getClass().getSimpleName())
				.collect(Collectors.toList());
		
		messageConverters.forEach(converter -> {
			log.info("\t > {}", converter.substring(converter.lastIndexOf(".") + 1));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(messageConverters));
	}
	
	@GetMapping("/exception-resolvers")
	public ResponseEntity<Object> exceptionResolvers() {
		HandlerExceptionResolverComposite composite = 
				webApplicationContext.getBean(HandlerExceptionResolverComposite.class);
		List<String> exceptionResolvers = composite.getExceptionResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getSimpleName())
				.collect(Collectors.toList());
		
		exceptionResolvers.forEach(resolver -> {
			log.info("\t > {}", resolver.substring(resolver.lastIndexOf(".") + 1));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(exceptionResolvers));
	}
	
	@GetMapping("/argument-resolvers")
	public ResponseEntity<Object> argumentResolvers() {
		RequestMappingHandlerAdapter handlerAdapter = webApplicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<String> argumentResolvers = handlerAdapter.getArgumentResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getSimpleName())
				.collect(Collectors.toList());
		
		argumentResolvers.forEach(resolver -> {
			log.info("\t > {}", resolver.substring(resolver.lastIndexOf(".") + 1));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(argumentResolvers));
	}

}
