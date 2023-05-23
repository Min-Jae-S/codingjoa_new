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
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.ViewResolverComposite;

import com.codingjoa.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/config")
@RestController
public class ConfigRestController {
	
	@Autowired // WebApplicationContext = ApplicationContext + getServletContext()
	private WebApplicationContext webApplicationContext;

	@GetMapping("/filters")
	public ResponseEntity<Object> getFilters() {
		log.info("## getFilters");
		FilterChainProxy filterChainProxy = webApplicationContext.getBean(FilterChainProxy.class);
		List<String> filters = filterChainProxy.getFilterChains()
			.stream()
			.filter(filterChain -> filterChain instanceof SecurityFilterChain)
			.findFirst().get().getFilters()
			.stream()
			.map(filter -> filter.getClass().getName())
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

	@GetMapping("/interceptors")
	public ResponseEntity<Object> getInterceptors() {
		log.info("## getInterceptors");
		Map<String, HandlerInterceptor> handlerInterceptorMap = 
				webApplicationContext.getBeansOfType(HandlerInterceptor.class);
		
		List<String> interceptors = handlerInterceptorMap.values()
				.stream()
				.map(interceptor -> interceptor.getClass().getName())
				.collect(Collectors.toList());
		
		interceptors.forEach(interceptor -> {
			log.info("\t > {}", interceptor.substring(interceptor.lastIndexOf(".") + 1));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(interceptors));
	}
	
	@GetMapping("/argument-resolvers")
	public ResponseEntity<Object> getArgumentResolvers() {
		log.info("## getArgumentResolvers");
		RequestMappingHandlerAdapter handlerAdapter = webApplicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<String> argumentResolvers = handlerAdapter.getArgumentResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getName())
				.collect(Collectors.toList());
		
		argumentResolvers.forEach(resolver -> { 
			log.info("\t > {}", resolver.substring(resolver.lastIndexOf(".") + 1));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(argumentResolvers));
	}

	@GetMapping("/return-value-handlers")
	public ResponseEntity<Object> getReturnValueHandler() {
		log.info("## getReturnValueHandler");
//		HandlerMethodReturnValueHandlerComposite composite = 
//				webApplicationContext.getBean(HandlerMethodReturnValueHandlerComposite.class);
//		composite.getHandlers().forEach(handler -> log.info("\t > return value handler = {}", handler.getClass()));

		RequestMappingHandlerAdapter handlerAdapter = webApplicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<String> returnValueHandlers = handlerAdapter.getReturnValueHandlers()
				.stream()
				.map(handler -> handler.getClass().getName())
				.collect(Collectors.toList());
		
		returnValueHandlers.forEach(handler -> {
			log.info("\t > {}", handler.substring(handler.lastIndexOf(".") + 1));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(returnValueHandlers));
	}

	@GetMapping("/view-resolvers")
	public ResponseEntity<Object> getViewResolvers() {
		log.info("## getViewResolvers");
		ViewResolverComposite composite = webApplicationContext.getBean(ViewResolverComposite.class);
		List<String> viewResolvers = composite.getViewResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getName())
				.collect(Collectors.toList());
		
		log.info("  - ViewResolvers from ViewResolverComposite.class");
		viewResolvers.forEach(resolver -> {
			log.info("\t > {}", resolver.substring(resolver.lastIndexOf(".") + 1));
		});
		
		Map<String, ViewResolver> viewResolverMap = 
				webApplicationContext.getBeansOfType(ViewResolver.class);
		log.info("  - ViewResolvers from ViewResolver.class");
		viewResolverMap.forEach((key, resolver) -> log.info("\t > {} : {}", key, resolver.getClass().getName()));
		
		return ResponseEntity.ok(SuccessResponse.create().data(viewResolvers));
	}
	
	@GetMapping("/message-converters")
	public ResponseEntity<Object> getMessageConverters() {
		log.info("## getMessageConverters");
		RequestMappingHandlerAdapter handlerAdapter = webApplicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<String> messageConverters = handlerAdapter.getMessageConverters()
				.stream()
				.map(converter -> converter.getClass().getName())
				.collect(Collectors.toList());
		
		messageConverters.forEach(converter -> {
			log.info("\t > {}", converter.substring(converter.lastIndexOf(".") + 1));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(messageConverters));
	}
	
	@GetMapping("/exception-resolvers")
	public ResponseEntity<Object> getExceptionResolvers() {
		log.info("## getExceptionResolvers");
		HandlerExceptionResolverComposite composite = 
				webApplicationContext.getBean(HandlerExceptionResolverComposite.class);
		List<String> exceptionResolvers = composite.getExceptionResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getName())
				.collect(Collectors.toList());
		
		log.info("  - ExceptionResolvers from HandlerExceptionResolverComposite.class");
		exceptionResolvers.forEach(resolver -> {
			log.info("\t > {}", resolver.substring(resolver.lastIndexOf(".") + 1));
		});
		
		return ResponseEntity.ok(SuccessResponse.create().data(exceptionResolvers));
	}
	
}
