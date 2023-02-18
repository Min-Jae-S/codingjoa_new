package com.codingjoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/reply")
@Controller
public class ReplyController {

	@GetMapping("/board/{boardIdx}/replies")
	public String readReplies(@PathVariable int boardIdx, Model model) {
		log.info("boardIdx={}", boardIdx);
		
		model.addAttribute("boardIdx", boardIdx);
		
		model.addAttribute("msg", "댓글 리스트 입니다.");
		
		return "reply/board-reply";
	}
}
