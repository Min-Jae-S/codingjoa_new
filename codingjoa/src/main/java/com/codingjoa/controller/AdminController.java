package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

	@GetMapping("/test")
	public String test() {
		log.info("admin test...");
		
		return "admin/test";
	}
	
	
}
