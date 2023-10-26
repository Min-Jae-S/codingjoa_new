package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestH2Controller {
	
	@GetMapping("/h2")
	public String main() {
		log.info("## h2 main");
		return "test/h2";
	}
	
}
