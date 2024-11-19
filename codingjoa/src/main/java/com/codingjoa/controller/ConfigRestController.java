package com.codingjoa.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.validation.Validator;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.ViewResolverComposite;

import com.codingjoa.dto.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/config")
@RequiredArgsConstructor
@RestController
public class ConfigRestController {
	
	private final WebApplicationContext webApplicationContext; // ApplicationContext + getServletContext()

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
				securityFilters.forEach(filter -> {
					int beginIndex = filter.lastIndexOf(".") + 1;
					log.info("\t\t - {}", filter.substring(beginIndex));
				});
			} catch (NoSuchBeanDefinitionException e) {
				filters.add(filterName);
			}
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().data(filters).build());
	}
	
	@GetMapping("/handler-mappings")
	public ResponseEntity<Object> getHandlerMappings() {
		log.info("## getHandlerMappings");
		Map<String, HandlerMapping> handlerMappingMap = webApplicationContext.getBeansOfType(HandlerMapping.class);
		handlerMappingMap.forEach((key, mapping) -> log.info("\t > {}: {}", key, mapping));
		
		List<Object> handlerMappings = new ArrayList<>();
		for (HandlerMapping handlerMapping : handlerMappingMap.values()) {
			String handlerMappingName =  handlerMapping.getClass().getName();
			if (handlerMapping instanceof RequestMappingHandlerMapping) {
				RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) handlerMapping;
				Set<String> handlers = mapping.getHandlerMethods().values()
					.stream()
					.map(handler -> handler.getClass().getName())
					.collect(Collectors.toSet());
				handlerMappings.add(Map.of(handlerMappingName, handlers));
			} else if (handlerMapping instanceof BeanNameUrlHandlerMapping) {
				BeanNameUrlHandlerMapping mapping = (BeanNameUrlHandlerMapping) handlerMapping;
				Map<String, Object> map = mapping.getHandlerMap();
				if (map.isEmpty()) {
					handlerMappings.add(handlerMappingName);
				} else {
					Set<String> handlers = mapping.getHandlerMap().values()
							.stream()
							.map(handler -> handler.getClass().getName())
							.collect(Collectors.toSet());
					handlerMappings.add(Map.of(handlerMappingName, handlers));
				}
			} else if (handlerMapping instanceof RouterFunctionMapping) {
				RouterFunctionMapping mapping = (RouterFunctionMapping) handlerMapping;
				RouterFunction<?> routerFunction = mapping.getRouterFunction();
				if (routerFunction == null) {
					handlerMappings.add(handlerMappingName);
				} else {
					handlerMappings.add(Map.of(handlerMappingName,routerFunction));
				}
			} else if (handlerMapping instanceof SimpleUrlHandlerMapping) {
				SimpleUrlHandlerMapping mapping = (SimpleUrlHandlerMapping) handlerMapping;
				Set<String> handlers = mapping.getHandlerMap().values()
						.stream()
						.map(handler -> handler.getClass().getName())
						.collect(Collectors.toSet());
				handlerMappings.add(Map.of(handlerMappingName, handlers));
			}
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().data(handlerMappings).build());
	}

	@GetMapping("/interceptors")
	public ResponseEntity<Object> getInterceptors() {
		log.info("## getInterceptors");
		Map<String, HandlerInterceptor> handlerInterceptorMap = webApplicationContext.getBeansOfType(HandlerInterceptor.class);
		handlerInterceptorMap.forEach((key, interceptor) -> 
			log.info("\t > {}: {}", key, interceptor.getClass().getName()));
		
		List<String> interceptors = handlerInterceptorMap.values()
				.stream()
				.map(interceptor -> interceptor.getClass().getName())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(SuccessResponse.builder().data(interceptors).build());
	}
	
	@GetMapping("/handler-adapters")
	public ResponseEntity<Object> getHandlerAdapters() {
		log.info("## getHandlerAdapters");
		Map<String, HandlerAdapter> handlerAdapterMap = webApplicationContext.getBeansOfType(HandlerAdapter.class);
		handlerAdapterMap.forEach((key, adapter) -> log.info("\t > {}: {}", key, adapter.getClass().getName()));
		
		List<String> handlerAdapters = handlerAdapterMap.values()
				.stream()
				.map(adapter -> adapter.getClass().getName())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(SuccessResponse.builder().data(handlerAdapters).build());
	}

	@GetMapping("/argument-resolvers")
	public ResponseEntity<Object> getArgumentResolvers() {
		log.info("## getArgumentResolvers");
		Map<String, HandlerMethodArgumentResolver> map = webApplicationContext.getBeansOfType(HandlerMethodArgumentResolver.class);
		log.info("\t > {}", map.keySet());
		
		RequestMappingHandlerAdapter handlerAdapter = webApplicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<String> argumentResolvers = handlerAdapter.getArgumentResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getName())
				.collect(Collectors.toList());
		
		argumentResolvers.forEach(resolver -> { 
			int beiginIndex = resolver.lastIndexOf(".") + 1;
			log.info("\t > {}", resolver.substring(beiginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.builder().data(argumentResolvers).build());
	}

	@GetMapping("/validators")
	public ResponseEntity<Object> getValidators() {
		log.info("## getValidators");
		Map<String, Validator> validatorMap = webApplicationContext.getBeansOfType(Validator.class);
		validatorMap.forEach((key, validator) -> log.info("\t > {}: {}", key, validator));
		
		return ResponseEntity.ok(SuccessResponse.builder().data(Collections.emptyList()).build());
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
			int beginIndex = handler.lastIndexOf(".") + 1;
			log.info("\t > {}", handler.substring(beginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.builder().data(returnValueHandlers).build());
	}

	@GetMapping("/view-resolvers")
	public ResponseEntity<Object> getViewResolvers() {
		log.info("## getViewResolvers");
		
//		ViewResolverComposite composite = webApplicationContext.getBean(ViewResolverComposite.class);
//		List<String> viewResolvers = composite.getViewResolvers()
//				.stream()
//				.map(resolver -> resolver.getClass().getName())
//				.collect(Collectors.toList());
		
		Map<String, ViewResolver> viewResolverMap = webApplicationContext.getBeansOfType(ViewResolver.class);
		viewResolverMap.forEach((key, resolver) -> log.info("\t > {}: {}", key, resolver.getClass().getName()));
		
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
		
		return ResponseEntity.ok(SuccessResponse.builder().data(viewResolvers).build());
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
			int beginIndex = converter.lastIndexOf(".") + 1;
			log.info("\t > {}", converter.substring(beginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.builder().data(messageConverters).build());
	}
	
	@GetMapping("/exception-resolvers")
	public ResponseEntity<Object> getExceptionResolvers() {
		log.info("## getExceptionResolvers");
		Map<String, HandlerExceptionResolver> exceptionResolverMap = webApplicationContext.getBeansOfType(HandlerExceptionResolver.class);
		HandlerExceptionResolverComposite composite = null;
		
		log.info("\t > ExceptionResolvers from HandlerExceptionResolver.class");
		for (HandlerExceptionResolver resolver : exceptionResolverMap.values()) {
			Object target = AopProxyUtils.getSingletonTarget(resolver);
			log.info("\t\t - {}", target.getClass().getName());
			if (target instanceof HandlerExceptionResolverComposite) {
				composite = (HandlerExceptionResolverComposite) target;
			}
		}
		
		List<String> exceptionResolvers = composite.getExceptionResolvers()
				.stream()
				.map(resolver -> resolver.getClass().getName())
				.collect(Collectors.toList());
		
		log.info("\t > ExceptionResolvers from HandlerExceptionResolverComposite.class");
		exceptionResolvers.forEach(resolver -> {
			int beginIndex = resolver.lastIndexOf(".") + 1;
			log.info("\t\t - {}", resolver.substring(beginIndex));
		});
		
		return ResponseEntity.ok(SuccessResponse.builder().data(exceptionResolvers).build());
	}
	
	@GetMapping("/object-mappers")
	public ResponseEntity<Object> getObjectMappers() {
		log.info("## getObjectMappers");
		Map<String, ObjectMapper> objectMapperMap = webApplicationContext.getBeansOfType(ObjectMapper.class);
		objectMapperMap.forEach((key, resolver) -> log.info("\t > {}: {}", key, resolver.getClass().getName()));
		
		return ResponseEntity.ok(SuccessResponse.builder().data(Collections.emptyList()).build());
	}
	
}
