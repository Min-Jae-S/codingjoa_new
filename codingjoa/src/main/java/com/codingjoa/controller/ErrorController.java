package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/error")
@Controller
public class ErrorController {
	
	@GetMapping("/errorPage")
	public String errorPage() {
		log.info("-------- error page --------");
		
		return "error/error-page";
	}
	
}
