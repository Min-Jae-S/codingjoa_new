package com.codingjoa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
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
//		FilterChainProxy filterChainProxy = webApplicationContext.getBean(FilterChainProxy.class);
//		List<String> filters = filterChainProxy.getFilterChains()
//			.stream()
//			.filter(filterChain -> filterChain instanceof SecurityFilterChain)
//			.findFirst().get().getFilters()
//			.stream()
//			.map(filter -> filter.getClass().getName())
//			.collect(Collectors.toList());
		
		ServletContext servletContext = webApplicationContext.getServletContext();
		Map<String, ? extends FilterRegistration> registrationMap = servletContext.getFilterRegistrations();
		List<Object> filters = new ArrayList<>();
		for (String filterName : registrationMap.keySet()) {
			log.info("\t > {}", filterName);
			try {
				FilterChainProxy filterChainProxy = (FilterChainProxy) webApplicationContext.getBean(filterName);
				List<SecurityFilterChain> filterChains = filterChainProxy.getFilterChains();
				List<String> securityFilters = filterChains.stream()
						.flatMap(filterChain -> filterChain.getFilters().stream())
						.map(filter -> filter.getClass().getName())
						.collect(Collectors.toList());
				filters.add(Map.of(filterName, securityFilters));
				securityFilters.forEach(filter -> 
					log.info("\t\t - {}", filter.substring(filter.lastIndexOf(".") + 1)));
			} catch (NoSuchBeanDefinitionException e) {
				filters.add(filterName);
			}
		}
		
		return ResponseEntity.ok(SuccessResponse.create().data(filters));
	}
	
	@GetMapping("/handler-mappings")
	public ResponseEntity<Object> getHandlerMappings() {
		log.info("## getHandlerMappings");
		Map<String, HandlerMapping> handlerMappingMap = webApplicationContext.getBeansOfType(HandlerMapping.class);
		handlerMappingMap.forEach((key, mapping) -> log.info("\t > {} : {}", key, mapping.getClass().getName()));
		
		List<String> handlerMappings = handlerMappingMap.values()
				.stream()
				.map(mapping -> mapping.getClass().getName())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(SuccessResponse.create().data(handlerMappings));
	}

	@GetMapping("/interceptors")
	public ResponseEntity<Object> getInterceptors() {
		log.info("## getInterceptors");
		Map<String, HandlerInterceptor> handlerInterceptorMap = 
				webApplicationContext.getBeansOfType(HandlerInterceptor.class);
		handlerInterceptorMap.forEach((key, interceptor) -> 
			log.info("\t > {} : {}", key, interceptor.getClass().getName()));
		
		List<String> interceptors = handlerInterceptorMap.values()
				.stream()
				.map(interceptor -> interceptor.getClass().getName())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(SuccessResponse.create().data(interceptors));
	}
	
	@GetMapping("/handler-adapters")
	public ResponseEntity<Object> getHandlerAdapters() {
		log.info("## getHandlerAdapters");
		Map<String, HandlerAdapter> handlerAdapterMap = webApplicationContext.getBeansOfType(HandlerAdapter.class);
		handlerAdapterMap.forEach((key, adapter) -> log.info("\t > {} : {}", key, adapter.getClass().getName()));
		
		List<String> handlerAdapters = handlerAdapterMap.values()
				.stream()
				.map(adapter -> adapter.getClass().getName())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(SuccessResponse.create().data(handlerAdapters));
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
		
//		ViewResolverComposite composite = webApplicationContext.getBean(ViewResolverComposite.class);
//		List<String> viewResolvers = composite.getViewResolvers()
//				.stream()
//				.map(resolver -> resolver.getClass().getName())
//				.collect(Collectors.toList());
		
		Map<String, ViewResolver> viewResolverMap = 
				webApplicationContext.getBeansOfType(ViewResolver.class);
		viewResolverMap.forEach((key, resolver) -> log.info("\t > {} : {}", key, resolver.getClass().getName()));
		
		List<String> viewResolvers = new ArrayList<>();
		for (ViewResolver viewResolver : viewResolverMap.values()) {
			if (viewResolver instanceof ViewResolverComposite) {
				ViewResolverComposite composite = (ViewResolverComposite) viewResolver;
				List<String> compositeResovlers = composite.getViewResolvers()
						.stream()
						.map(resolver -> resolver.getClass().getName())
						.collect(Collectors.toList());
				viewResolvers.addAll(compositeResovlers);
			} else {
				viewResolvers.add(viewResolver.getClass().getName());
			}
		}
		
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
		
		Map<String, HandlerExceptionResolver> exceptionResolverMap = 
				webApplicationContext.getBeansOfType(HandlerExceptionResolver.class);
		log.info("  - ExceptionResolvers from HandlerExceptionResolver.class");
		exceptionResolverMap.forEach((key, resolver) -> log.info("\t > {} : {}", key, resolver.getClass().getName()));
		
		return ResponseEntity.ok(SuccessResponse.create().data(exceptionResolvers));
	}
	
}
