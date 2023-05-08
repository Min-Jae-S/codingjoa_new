package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/error")
@Controller
public class ErrorController {

	@RequestMapping("/errorPage")
	public String errorPage() {
		log.info("ERROR PAGE...");
		
		return "error/error-page";
	}
	
}
