package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/board")
@Controller
public class BoardController {
	
	
	@RequestMapping("")
	public String board(@RequestParam("categoryCode") int categoryCode) {
		log.info("{}", categoryCode);
		
		return null;
	}
}
