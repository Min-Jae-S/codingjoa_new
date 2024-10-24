package com.codingjoa.controller.test;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.annotation.AnnoTest;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.impl.EmailServiceImpl;
import com.codingjoa.service.test.TestProxyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/api/aop")
@RestController
public class TestAopRestController {
	
	private final String regex = "\\.(?=[^.]+$)";
	
	@Autowired
	private TestAopRestController self;

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private TestProxyService testProxyService;
	
	@Autowired
	private EmailServiceImpl emailService;
	
	@GetMapping("/exception")
	public void triggerExceptionByAjax() {
		log.info("## triggerExceptionByAjax");
		throw new RuntimeException();
	}

	@GetMapping("/exception/controller")
	public ResponseEntity<Object> triggerExceptionInController() {
		log.info("## triggerExceptionInController");
		throw new RuntimeException();
	}
	
	@GetMapping("/exception/interceptor")
	public ResponseEntity<Object> triggerExceptionInInterceptor() {
		log.info("## triggerExceptionInInterceptor");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/exception/filter")
	public ResponseEntity<Object> triggerExceptionInFilter() {
		log.info("## triggerExceptionInFilter");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		log.info("\t > testAopRestController = {}", self.getClass().getName().split(regex, 2)[1]);
		log.info("\t\t - isAopProxy = {}", AopUtils.isAopProxy(self));
		log.info("\t\t - isJdkDynamicProxy = {}", AopUtils.isJdkDynamicProxy(self));
		log.info("\t\t - isCglibProxy = {}", AopUtils.isCglibProxy(self));
		
		log.info("\t > testProxyService = {}", testProxyService.getClass().getName().split(regex, 2)[1]);
		log.info("\t\t - isAopProxy = {}", AopUtils.isAopProxy(testProxyService));
		log.info("\t\t - isJdkDynamicProxy = {}", AopUtils.isJdkDynamicProxy(testProxyService));
		log.info("\t\t - isCglibProxy = {}", AopUtils.isCglibProxy(testProxyService));

		log.info("\t > memberService = {}", memberService.getClass().getName().split(regex, 2)[1]);
		log.info("\t\t - isAopProxy = {}", AopUtils.isAopProxy(memberService));
		log.info("\t\t - isJdkDynamicProxy = {}", AopUtils.isJdkDynamicProxy(memberService));
		log.info("\t\t - isCglibProxy = {}", AopUtils.isCglibProxy(memberService));
		
		log.info("\t > emailService = {}", emailService.getClass().getName().split(regex, 2)[1]);
		log.info("\t\t - isAopProxy = {}", AopUtils.isAopProxy(emailService));
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@AnnoTest
	@GetMapping("/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		log.info("\t > testAopRestController = {}", self.getClass().getName().split(regex, 2)[1]);
		log.info("\t > testAopRestController, isAopProxy = {}", AopUtils.isAopProxy(self));
		log.info("\t > testProxyService = {}", testProxyService.getClass().getName().split(regex, 2)[1]);
		log.info("\t > testProxyService, isAopProxy = {}", AopUtils.isAopProxy(testProxyService));
		testProxyService.test();
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	
	
}
