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
public class ConfigController {
	
	@Autowired // WebApplicationContext = ApplicationContext + getServletContext()
	private WebApplicationContext applicationCtx;
	
	@GetMapping("/filters")
	public ResponseEntity<Object> filters() {
		FilterChainProxy filterChainProxy = applicationCtx.getBean(FilterChainProxy.class);
		SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(0);
		List<String> filters = securityFilterChain.getFilters()
				.stream()
				.map(filter -> filter.getClass().getSimpleName())
				.collect(Collectors.toList());

		ServletContext servletCtx = applicationCtx.getServletContext();
		Map<String, ? extends FilterRegistration> map = servletCtx.getFilterRegistrations();
		for (String filterName : map.keySet()) {
			if (!"springSecurityFilterChain".equals(filterName)) {
				filters.add(filterName);
			}
		}

		filters.forEach(filter -> {
			int beginIndex = filter.lastIndexOf(".") + 1;
			log.info("\t > {}", filter.substring(beginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(filters));
	}

	@GetMapping("/message-converters")
	public ResponseEntity<Object> messageConverters() {
		RequestMappingHandlerAdapter handlerAdapter = applicationCtx.getBean(RequestMappingHandlerAdapter.class);
		List<String> converters = handlerAdapter.getMessageConverters()
				.stream()
				.map(converter -> converter.getClass().getSimpleName())
				.collect(Collectors.toList());
		
		converters.forEach(converter -> {
			int beginIndex = converter.lastIndexOf(".") + 1;
			log.info("\t > {}", converter.substring(beginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(converters));
	}
	
	@GetMapping("/exception-resolvers")
	public ResponseEntity<Object> exceptionResolvers() {
		HandlerExceptionResolverComposite composite = applicationCtx.getBean(HandlerExceptionResolverComposite.class);
		List<String> resolvers = composite.getExceptionResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getSimpleName())
				.collect(Collectors.toList());
		
		resolvers.forEach(resolver -> {
			int beginIndex = resolver.lastIndexOf(".") + 1;
			log.info("\t > {}", resolver.substring(beginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(resolvers));
	}
	
	@GetMapping("/argument-resolvers")
	public ResponseEntity<Object> argumentResolvers() {
		RequestMappingHandlerAdapter handlerAdapter = applicationCtx.getBean(RequestMappingHandlerAdapter.class);
		List<String> resolvers = handlerAdapter.getArgumentResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getSimpleName())
				.collect(Collectors.toList());
		
		resolvers.forEach(resolver -> {
			int beginIndex = resolver.lastIndexOf(".") + 1;
			log.info("\t > {}", resolver.substring(beginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(resolvers));
	}

}
