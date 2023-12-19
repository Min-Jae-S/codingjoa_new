package com.codingjoa.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestViewController {

	@GetMapping("/batch")
	public String batchMain() {
		log.info("## batch main");
		log.info("\t > return test/batch.jsp");
		return "test/batch";
	}
	
	@GetMapping("/quartz")
	public String quartzMain() {
		log.info("## quartz main");
		log.info("\t > return test/quartz.jsp");
		return "test/quartz";
	}
}
