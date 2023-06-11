package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/error")
@Controller
public class ErrorController {
	
	@RequestMapping("/errorPage")
	public String errorPage(HttpServletRequest request) {
		log.info("## errorPage");
		log.info("\t > {}", request.getAttribute("errorResponse"));
		
		return "error/error-page";
	}
	
}
