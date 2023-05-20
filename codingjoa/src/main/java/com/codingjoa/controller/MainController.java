package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

	@GetMapping("/")
	public String home() {
		log.info("-------- HOME PAGE --------");
		return "home";
	}
	
	@GetMapping("/accessDenied")
	public String accessDenied() {
		log.info("-------- ACCESS DENIED --------");
		return "access-denied";
	}
	
	@GetMapping("/board")
	public String board() {
		return "redirect:/board/all";
	}
	


	
}
