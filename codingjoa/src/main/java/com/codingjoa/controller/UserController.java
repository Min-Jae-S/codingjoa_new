package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class UserController {

	@GetMapping("/account")
	public String account() {
		log.info("## account");
		return "user/account";
	}
	
}
