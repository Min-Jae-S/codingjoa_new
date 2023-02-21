package com.codingjoa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/reply")
@RestController
public class ReplyRestController {

	@GetMapping("/{boardIdx}/replies")
	public String readReplies(@PathVariable int boardIdx) {
		log.info("boardIdx={}", boardIdx);
		
		return "abc";
	}
}
