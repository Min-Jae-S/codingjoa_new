package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestController {
	
	@RequestMapping("/a")
	public String a() {
		log.info("## a called...");
		
		return "forward:/test/b";
	}
	
	@ResponseBody
	@RequestMapping("/b")
	public String b() {
		log.info("## b called...");
		
		return "b";
	}
}
