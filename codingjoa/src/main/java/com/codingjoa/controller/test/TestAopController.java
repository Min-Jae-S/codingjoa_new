package com.codingjoa.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingjoa.test.TestException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/aop")
@Controller
public class TestAopController {

	@GetMapping("/exception")
	public void triggerExceptionByMvc() {
		log.info("## triggerExceptionByMvc");
		throw new RuntimeException("exception by mvc");
	}
	
	@GetMapping("/exception/controller")
	public void triggerExceptionInController() {
		log.info("## triggerExceptionInController");
		throw new RuntimeException("exception in controller by mvc");
	}
	
	@GetMapping("/exception/interceptor")
	public String triggerExceptionInInterceptor() {
		log.info("## triggerExceptionInInterceptor");
		return "home";
	}
	
	@GetMapping("/exception/filter")
	public String triggerExceptionInFilter() {
		log.info("## triggerExceptionInFilter");
		return "home";
	}

	@GetMapping("/test5")
	public String test5() {
		log.info("## test5");
		return "nonExistingView";
	}

	@GetMapping("/test6")
	public String test6() {
		log.info("## test6");
		throw new TestException("testException");
	}
	
}
