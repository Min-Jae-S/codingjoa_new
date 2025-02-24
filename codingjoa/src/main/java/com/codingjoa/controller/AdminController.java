package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AdminController {
	
	@GetMapping("/admin/**")
	public String main() {
		log.info("## main");
		return "admin/main";
	}
	
	
}
