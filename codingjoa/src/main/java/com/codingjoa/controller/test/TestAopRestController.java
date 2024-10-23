package com.codingjoa.controller.test;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.annotation.AspectTest;
import com.codingjoa.aop.test.TestAspect;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.impl.EmailServiceImpl;
import com.codingjoa.service.impl.MemberServiceImpl;
import com.codingjoa.service.test.TestProxyService;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequestMapping("/test/api/aop")
@RestController
public class TestAopRestController {

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private TestProxyService testProxyService;
	
//	@Autowired
//	private MemberServiceImpl memberServiceImpl;

	@Autowired
	private EmailServiceImpl emailServiceImpl;
	
	@ModelAttribute
	public void loggingBeforeMethod() {
		log.info("## loggingBeforeMethod");
		log.info("\t > isAopProxy, this from inner controller= {}", AopUtils.isAopProxy(this));
		TestAopRestController controller = context.getBean(TestAopRestController.class);
		log.info("\t > isAopProxy, this from context = {}", AopUtils.isAopProxy(controller));
	}
	
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
	
	@AspectTest
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		TestAspect testAspect = context.getBean(TestAspect.class);
		log.info("\t > aspect = {}", testAspect);
		log.info("\t > isAopProxy, testAspect = {}", AopUtils.isAopProxy(testAspect));
		
		log.info("\t > isAopProxy, memberService = {}", AopUtils.isAopProxy(memberService));
		log.info("\t > isJdkDynamicProxy, memberService = {}", AopUtils.isJdkDynamicProxy(memberService));
		log.info("\t > isCglibProxy, memberService = {}", AopUtils.isCglibProxy(memberService));
		
		log.info("\t > isAopProxy, testProxyService = {}", AopUtils.isAopProxy(testProxyService));
		log.info("\t > isJdkDynamicProxy, testProxyService = {}", AopUtils.isJdkDynamicProxy(testProxyService));
		log.info("\t > isCglibProxy, testProxyService = {}", AopUtils.isCglibProxy(testProxyService));
		
		//log.info("\t > isAopProxy, emailServiceImpl = {}", AopUtils.isAopProxy(emailServiceImpl));
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
}
