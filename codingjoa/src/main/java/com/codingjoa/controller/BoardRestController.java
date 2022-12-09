package com.codingjoa.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/board")
@RestController
public class BoardRestController {
	
	@PostMapping("/uploadImage")
	public String uploadImage() {
		
		return "";
	}
	
}
