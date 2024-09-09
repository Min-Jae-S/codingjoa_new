package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ErrorController {
	
	@RequestMapping("/error")
	public String error(HttpServletRequest request) {
		log.info("## error");
		log.info("\t > {}", request.getAttribute("errorResponse"));
		return "error";
	}
	
}
