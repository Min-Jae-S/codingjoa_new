package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/error")
@Controller
public class ErrorController {
	
	@GetMapping("/errorPage")
	public String errorPage(HttpServletRequest request) {
		log.info("## errorPage");
		log.info("\t > errorMessage = {}", request.getAttribute("errorMessage"));
		
		return "error/error-page";
	}
	
}
