package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.codingjoa.dto.LoginDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

	@GetMapping("/")
	public String home() {
		log.info("## home");
		return "home";
	}
	
	@GetMapping("/accessDenied")
	public String accessDenied() {
		log.info("## accessDenied");
		return "access-denied";
	}
	
	// GET : login form
	@GetMapping("/login") 
	public String loginForm(@ModelAttribute LoginDto loginDto) {
		log.info("## loginForm");
		return "member/login";
	}
	
	// POST : login failure --> forward from LoginFailureHandler
	@PostMapping("/login")
	public String loginFailureForm(@ModelAttribute LoginDto loginDto, HttpServletRequest request) {
		log.info("## loginFailureForm");
		log.info("\t > {}", loginDto);
		log.info("\t > {}", request.getAttribute("errorResponse"));
		return "member/login";
	}
	
}
