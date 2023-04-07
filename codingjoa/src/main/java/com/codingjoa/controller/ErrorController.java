package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/error")
@Controller
public class ErrorController {

	@RequestMapping("/errorPage")
	public String errorPage() {
		log.info("errorPage");
		return "error/error-page";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/422")
	@ResponseBody
	public ResponseEntity<Object> error422(HttpServletRequest request) {
		log.info("error422");
		return (ResponseEntity<Object>) request.getAttribute("errorResponse");
	}
}
