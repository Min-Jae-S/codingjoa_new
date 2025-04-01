package com.codingjoa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.validation.Validator;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
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
@RequestMapping("/api/config")
@RequiredArgsConstructor
@RestController
public class ConfigRestController {
	
	private final WebApplicationContext context; // ApplicationContext + getServletContext()

	@GetMapping("/filters")
	public ResponseEntity<Object> getFilters() {
		log.info("## getFilters");
//		FilterChainProxy filterChainProxy = context.getBean(FilterChainProxy.class);
//		List<String> filters = filterChainProxy.getFilterChains()
//			.stream()
//			.filter(filterChain -> filterChain instanceof SecurityFilterChain)
//			.findFirst().get().getFilters()
//			.stream()
//			.map(filter -> filter.getClass().getName())
//			.collect(Collectors.toList());
		
		ServletContext servletContext = context.getServletContext();
		Map<String, ? extends FilterRegistration> map = servletContext.getFilterRegistrations();
		List<Object> filters = new ArrayList<>();
		for (String filterName : map.keySet()) {
			log.info("\t > {}", filterName);
			try {
				FilterChainProxy filterChainProxy = (FilterChainProxy) context.getBean(filterName);
				List<SecurityFilterChain> filterChains = filterChainProxy.getFilterChains();
				List<String> securityFilters = filterChains.stream()
						.flatMap(filterChain -> filterChain.getFilters().stream())
						.map(filter -> filter.getClass().getName())
						.collect(Collectors.toList());
				filters.add(Map.of(filterName, securityFilters));
				securityFilters.forEach(filter -> log.info("\t\t - {}", filter.substring(filter.lastIndexOf(".") + 1)));
			} catch (NoSuchBeanDefinitionException e) {
				filters.add(filterName);
			}
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().data(filters).build());
	}
	
	@GetMapping("/handler-mappings")
	public ResponseEntity<Object> getHandlerMappings() {
		log.info("## getHandlerMappings");
		Map<String, HandlerMapping> handlerMappingMap = context.getBeansOfType(HandlerMapping.class);
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
		Map<String, HandlerInterceptor> map = context.getBeansOfType(HandlerInterceptor.class);
		map.forEach((key, interceptor) -> log.info("\t > {}: {}", key, interceptor.getClass().getName()));
		
		List<String> interceptors = map.values()
				.stream()
				.map(interceptor -> interceptor.getClass().getName())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(SuccessResponse.builder().data(interceptors).build());
	}
	
	@GetMapping("/handler-adapters")
	public ResponseEntity<Object> getHandlerAdapters() {
		log.info("## getHandlerAdapters");
		Map<String, HandlerAdapter> map = context.getBeansOfType(HandlerAdapter.class);
		map.forEach((key, adapter) -> log.info("\t > {}: {}", key, adapter.getClass().getName()));
		
		List<String> handlerAdapters = map.values()
				.stream()
				.map(adapter -> adapter.getClass().getName())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(SuccessResponse.builder().data(handlerAdapters).build());
	}

	@GetMapping("/argument-resolvers")
	public ResponseEntity<Object> getArgumentResolvers() {
		log.info("## getArgumentResolvers");
		Map<String, HandlerMethodArgumentResolver> map = context.getBeansOfType(HandlerMethodArgumentResolver.class);
		log.info("\t > {}", map);
		
		log.info("\t > resolvers from RequestMappingHandlerAdapter");
		RequestMappingHandlerAdapter handlerAdapter = context.getBean(RequestMappingHandlerAdapter.class);
		List<String> argumentResolvers = handlerAdapter.getArgumentResolvers()
				.stream()
				.map(resolver -> {
					String className = resolver.getClass().getName();
					log.info("\t\t - {}", className.substring(className.lastIndexOf(".") + 1));
					return className;
				})
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(SuccessResponse.builder().data(argumentResolvers).build());
	}

	@GetMapping("/validators")
	public ResponseEntity<Object> getValidators() {
		log.info("## getValidators");
		Map<String, Validator> map = context.getBeansOfType(Validator.class);
		Map<String, String> validators = map.entrySet().stream()
				.peek(entry -> {
					String className = entry.getValue().getClass().getName();
					log.info("\t > {}: {}", entry.getKey(), className.substring(className.lastIndexOf(".") + 1));
				})
				.collect(Collectors.toMap(
						entry -> entry.getKey(), 
						entry -> entry.getValue().getClass().getName()
				));
		
		return ResponseEntity.ok(SuccessResponse.builder().data(validators).build());
	}

	@GetMapping("/return-value-handlers")
	public ResponseEntity<Object> getReturnValueHandler() {
		log.info("## getReturnValueHandler");
//		HandlerMethodReturnValueHandlerComposite composite = 
//				webApplicationContext.getBean(HandlerMethodReturnValueHandlerComposite.class);
//		composite.getHandlers().forEach(handler -> log.info("\t > return value handler = {}", handler.getClass()));

		RequestMappingHandlerAdapter handlerAdapter = context.getBean(RequestMappingHandlerAdapter.class);
		List<String> handlers = handlerAdapter.getReturnValueHandlers()
				.stream()
				.map(handler -> {
					String className = handler.getClass().getName();
					log.info("\t\t - {}", className.substring(className.lastIndexOf(".") + 1));
					return className;
				})
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(SuccessResponse.builder().data(handlers).build());
	}

	@GetMapping("/view-resolvers")
	public ResponseEntity<Object> getViewResolvers() {
		log.info("## getViewResolvers");
		
//		ViewResolverComposite composite = webApplicationContext.getBean(ViewResolverComposite.class);
//		List<String> viewResolvers = composite.getViewResolvers()
//				.stream()
//				.map(resolver -> resolver.getClass().getName())
//				.collect(Collectors.toList());
		
		Map<String, ViewResolver> map = context.getBeansOfType(ViewResolver.class);
		map.forEach((key, resolver) -> log.info("\t > {}: {}", key, resolver.getClass().getName()));
		
		List<String> viewResolvers = new ArrayList<>();
		
		for (ViewResolver viewResolver : map.values()) {
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
		RequestMappingHandlerAdapter handlerAdapter = context.getBean(RequestMappingHandlerAdapter.class);
		List<String> messageConverters = handlerAdapter.getMessageConverters()
				.stream()
				.map(converter -> {
					String className = converter.getClass().getName();
					log.info("\t\t - {}", className.substring(className.lastIndexOf(".") + 1));
					return className;
				})
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(SuccessResponse.builder().data(messageConverters).build());
	}
	
	@GetMapping("/exception-resolvers")
	public ResponseEntity<Object> getExceptionResolvers() {
		log.info("## getExceptionResolvers");
		Map<String, HandlerExceptionResolver> map = context.getBeansOfType(HandlerExceptionResolver.class);
		HandlerExceptionResolverComposite composite = null;
		
		log.info("\t > resolvers from HandlerExceptionResolver");
		for (HandlerExceptionResolver resolver : map.values()) {
			Object target = AopUtils.isAopProxy(resolver) ? AopProxyUtils.getSingletonTarget(resolver) : resolver;
			log.info("\t\t - {}", target.getClass().getName());
			
			if (target instanceof HandlerExceptionResolverComposite) {
				composite = (HandlerExceptionResolverComposite) resolver;
			}
		}
		
		log.info("\t > resolvers from HandlerExceptionResolverComposite");
		List<String> exceptionResolvers = new ArrayList<>();

		if (composite != null) {
			exceptionResolvers = composite.getExceptionResolvers()
					.stream()
					.map(resolver -> {
						String className = resolver.getClass().getName();
						log.info("\t\t - {}", className.substring(className.lastIndexOf(".") + 1));
						return className;
					})
					.collect(Collectors.toList());
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().data(exceptionResolvers).build());
	}
	
	@GetMapping("/object-mappers")
	public ResponseEntity<Object> getObjectMappers() {
		log.info("## getObjectMappers");
		
		Map<String, ObjectMapper> map = new HashMap<>();
		ApplicationContext applicationContext = context.getParent();
		if (applicationContext != null) {
			map = applicationContext.getBeansOfType(ObjectMapper.class);
		}

		Map<String, String> mappers = map.entrySet().stream()
				.peek(entry -> {
					String className = entry.getValue().getClass().getName();
					log.info("\t > {}: {}", entry.getKey(), className.substring(className.lastIndexOf(".") + 1));
				})
				.collect(Collectors.toMap(
						entry -> entry.getKey(), 
						entry -> entry.getValue().getClass().getName()
				));
		
		return ResponseEntity.ok(SuccessResponse.builder().data(mappers).build());
	}
	
	@GetMapping("/beans")
	public ResponseEntity<Object> getBeans() {
		log.info("## getBeans");
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
		List<String> userDefinedBeans = new ArrayList<>();
		List<String> springInternalBeans = new ArrayList<>();
		
		ApplicationContext appContext = context.getParent();
		log.info("\t > parent context = {}", appContext);
		log.info("\t > parent beanFactory = {}", context.getParentBeanFactory().getClass());
		
		if (appContext != null) {
			String[] beanNames = appContext.getBeanDefinitionNames();
			for (String beanName : beanNames) {
				log.info("\t\t - {}", beanName);
			}
		}
		
		String[] beanNames = context.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
			if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
				userDefinedBeans.add(beanName);
			} else if (beanDefinition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE) {
				springInternalBeans.add(beanName);
			}
		}
		
		log.info("\t > servletContext, BeanDefinition.ROLE_APPLICATION");
		userDefinedBeans.forEach(s -> log.info("\t\t - {}", s));

		log.info("\t > servletContext, BeanDefinition.ROLE_INFRASTRUCTURE");
		springInternalBeans.forEach(s -> log.info("\t\t - {}", s));
		
		return ResponseEntity.ok(SuccessResponse.builder().build());
	}
	
	@SuppressWarnings("unused")
	private String getRoleDescription(int role) {
		switch (role) {
		case BeanDefinition.ROLE_APPLICATION: 		// user-defined bean (0)
			return "ROLE_APPLICATION";
		case BeanDefinition.ROLE_SUPPORT: 			// support bean (1)
			return "ROLE_SUPPORT";
		case BeanDefinition.ROLE_INFRASTRUCTURE: 	// spring-internal bean (2)
			return "ROLE_INFRASTRUCTURE";
		default:
			return "UNKNOWN";
		}
	}
	
}
