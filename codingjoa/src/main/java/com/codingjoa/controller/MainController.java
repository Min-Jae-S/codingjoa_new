package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/accessDenied")
	public String accessDenied() {
		return "access-denied";
	}
	
	@GetMapping("/board")
	public String board() {
		return "redirect:/board/all";
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
