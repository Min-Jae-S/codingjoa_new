package com.codingjoa.controller.test;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.codingjoa.annotation.AnnoTest;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.service.EmailService;
import com.codingjoa.service.UserService;
import com.codingjoa.service.test.TestProxyService;
import com.codingjoa.test.Sample;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/api/aop")
@RestController
public class TestAopRestController {
	
	private final String regexp = "\\.(?=[^.]+$)";
	
	@Autowired
	private TestAopRestController self;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TestProxyService testProxyService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired(required = false)
	private ExceptionHandlerMethodResolver exceptionHandlerMethodResolver;
	
	@GetMapping("/exception")
	public void triggerExceptionByAjax() {
		log.info("## triggerExceptionByAjax");
		throw new RuntimeException("exception by ajax");
	}

	@GetMapping("/exception/controller")
	public ResponseEntity<Object> triggerExceptionInController() {
		log.info("## triggerExceptionInController");
		throw new RuntimeException("exceptin in controller by ajax");
	}
	
	@GetMapping("/exception/interceptor")
	public ResponseEntity<Object> triggerExceptionInInterceptor() {
		log.info("## triggerExceptionInInterceptor");
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/exception/filter")
	public ResponseEntity<Object> triggerExceptionInFilter() {
		log.info("## triggerExceptionInFilter");
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/exception-handler")
	public ResponseEntity<Object> getExceptionHandler() {
		log.info("## getExceptionHandler");
		ExceptionHandlerExceptionResolver exceptionResolver = null;
		Map<String, HandlerExceptionResolver> exceptionResolverMap = context.getBeansOfType(HandlerExceptionResolver.class);
		
		for (Map.Entry<String, HandlerExceptionResolver> map : exceptionResolverMap.entrySet()) {
			HandlerExceptionResolver obj = map.getValue();
			boolean isAopProxy = AopUtils.isAopProxy(obj);
			if (isAopProxy) {
				obj = (HandlerExceptionResolver) AopProxyUtils.getSingletonTarget(obj);
			}
			
			log.info("\t > {} (isAopProxy = {})", obj.getClass().getSimpleName(), isAopProxy);
			
			if (obj instanceof HandlerExceptionResolverComposite) {
				HandlerExceptionResolverComposite composite = (HandlerExceptionResolverComposite) obj;
				for (HandlerExceptionResolver resolver : composite.getExceptionResolvers()) {
					boolean isResolverAopPrxoy = AopUtils.isAopProxy(obj);
					if (isResolverAopPrxoy) {
						resolver = (HandlerExceptionResolver) AopProxyUtils.getSingletonTarget(resolver);
					}
					
					log.info("\t\t - {} (isAopProxy = {})", resolver.getClass().getSimpleName(), isResolverAopPrxoy);
					
					if (resolver instanceof ExceptionHandlerExceptionResolver) {
						exceptionResolver = (ExceptionHandlerExceptionResolver) resolver;
						log.info("\t > exceptionResolver = {}", exceptionResolver);
					}
				}
			}
		}
		
		if (exceptionResolver != null) {
			log.info("\t > exceptionHandlerAdviceCache");
			exceptionResolver.getExceptionHandlerAdviceCache().forEach((key, methodResolver) -> {
				log.info("\t\t - {}: {} (isAopProxy = {})", 
						key, methodResolver.getClass().getSimpleName(), AopUtils.isAopProxy(methodResolver));
			});
		}
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		log.info("\t > testAopRestController = {}", self.getClass().getName().split(regexp, 2)[1]);
		log.info("\t\t - isAopProxy = {}", AopUtils.isAopProxy(self));
		log.info("\t\t - isJdkDynamicProxy = {}", AopUtils.isJdkDynamicProxy(self));
		log.info("\t\t - isCglibProxy = {}", AopUtils.isCglibProxy(self));
		
		log.info("\t > testProxyService = {}", testProxyService.getClass().getName().split(regexp, 2)[1]);
		log.info("\t\t - isAopProxy = {}", AopUtils.isAopProxy(testProxyService));
		log.info("\t\t - isJdkDynamicProxy = {}", AopUtils.isJdkDynamicProxy(testProxyService));
		log.info("\t\t - isCglibProxy = {}", AopUtils.isCglibProxy(testProxyService));

		log.info("\t > memberService = {}", userService.getClass().getName().split(regexp, 2)[1]);
		log.info("\t\t - isAopProxy = {}", AopUtils.isAopProxy(userService));
		log.info("\t\t - isJdkDynamicProxy = {}", AopUtils.isJdkDynamicProxy(userService));
		log.info("\t\t - isCglibProxy = {}", AopUtils.isCglibProxy(userService));
		
		log.info("\t > emailService = {}", emailService.getClass().getName().split(regexp, 2)[1]);
		log.info("\t\t - isAopProxy = {}", AopUtils.isAopProxy(emailService));
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@AnnoTest
	@GetMapping("/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		log.info("\t > testAopRestController = {},", self.getClass().getName().split(regexp, 2)[1]);
		log.info("\t > testAopRestController isAopProxy = {}", AopUtils.isAopProxy(self));
		log.info("\t > testProxyService = {}", testProxyService.getClass().getName().split(regexp, 2)[1]);
		log.info("\t > testProxyService isAopProxy = {}", AopUtils.isAopProxy(testProxyService));
		testProxyService.test();
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/test3")
	public ResponseEntity<Object> test3() {
		log.info("## test3");
		Map<String, ExceptionHandlerMethodResolver> map = context.getBeansOfType(ExceptionHandlerMethodResolver.class);
		log.info("\t > exceptionHandlerMethodResolver from context = {}", map);
		log.info("\t > exceptionHandlerMethodResolver from @autowired = {}", exceptionHandlerMethodResolver);
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/test4")
	public ResponseEntity<Object> test4(HttpServletResponse response) throws IOException {
		log.info("## test4");
		response.sendError(HttpServletResponse.SC_BAD_GATEWAY);
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/exception/async")
	public ResponseEntity<Object> triggerAsyncEx(HttpServletResponse response) throws IOException {
		log.info("## triggerAsyncEx");
		emailService.triggerAsyncEx(Sample.create(), "test");
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
}
