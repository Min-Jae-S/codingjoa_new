package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		log.info("============== home ==============");
		
		return "home";
	}
	
	@GetMapping("/accessDenied")
	public String accessDenied() {
		log.info("============== accessDenied ==============");
		
		return "access-denied";
	}
	

//	@GetMapping("/test")
//	public String test(Authentication authentication) {
//		log.info("============== test ==============");
//		
//		if(authentication == null) {
//			log.info("authentication is null");
//			
//			return "home";
//		} 
//		
//		Object principal = authentication.getPrincipal();
//		
//		if(principal instanceof UserDetails) {
//			UserDetails userDetails = (UserDetails) principal;
//			log.info("userDetails = {}", userDetails);
//		} else {
//			String memberId = (String) principal;
//			log.info("memberId = {}", memberId);
//		}
//		return "home";
//	}
	
}
