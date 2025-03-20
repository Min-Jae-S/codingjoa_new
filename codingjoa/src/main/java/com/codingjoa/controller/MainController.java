package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codingjoa.util.FormatUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {
	
	@GetMapping
	public String main() {
		log.info("## main");
		return "main";
	}

	@GetMapping("/login") 
	public String login(@RequestParam(name = "continue", required = false) String continueUrl, Model model) {
		log.info("## login");
		log.info("\t > continueUrl = {}", FormatUtils.formatString(continueUrl));
		model.addAttribute("continueUrl", continueUrl);
		return "login";
	}
	
	@GetMapping("/findPassword")
	public String findPassword() {
		log.info("## findPassword");
		return "user/find-password";
	}
	
	@GetMapping("/resetPassword") // pre-check key parameter in interceptor
	public String resetPassword(@RequestParam String key, Model model) {
		log.info("## resetPassword");
		model.addAttribute("key", key);
		return "user/reset-password";
	}
	
}
