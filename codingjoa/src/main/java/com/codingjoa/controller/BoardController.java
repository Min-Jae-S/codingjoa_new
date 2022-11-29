package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/board")
@Controller
public class BoardController {
	
	
	@GetMapping("")
	public String board(@RequestParam(name = "categoryCode", defaultValue = "0") int boardCategoryCode) {
		log.info("{}", boardCategoryCode);
		
		return "";
	}
	
	@GetMapping("/read")
	public String read() {
		return "";
	}
	
	@GetMapping("/write")
	public String write() {
		return "";
	}
	
	@PostMapping("/writeProc")
	public String writeProc() {
		return "";
	}
}
