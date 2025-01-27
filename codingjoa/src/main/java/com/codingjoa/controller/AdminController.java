package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/admin")
@Controller
public class AdminController {
	
	@GetMapping
	public String main() {
		log.info("## main");
		return "admin/admin-main";
	}
	
	@GetMapping("/index")
	public String index() {
		log.info("## main");
		return "admin/index";
	}
	
}
