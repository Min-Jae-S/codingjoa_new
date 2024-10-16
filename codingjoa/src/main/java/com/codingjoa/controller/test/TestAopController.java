package com.codingjoa.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/aop")
@Controller
public class TestAopController {

	@GetMapping("/exception")
	public void triggerExceptionByMvc() {
		log.info("## triggerExceptionByMvc");
		throw new RuntimeException();
	}
	
}
