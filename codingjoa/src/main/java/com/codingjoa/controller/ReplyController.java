package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/reply")
@Controller
public class ReplyController {

	@GetMapping("/board/{boardIdx}/replies")
	public void getReplies(@PathVariable int boardIdx) {
		log.info("boardIdx={}", boardIdx);
		
		
	}
}
