package com.codingjoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codingjoa.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/board")
@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/main")
	public String main(@RequestParam("categoryCode") int boardCategoryCode, Model model) {
		log.info("{}", boardCategoryCode);
		
		model.addAttribute("boardCategoryCode", boardCategoryCode);
		
		return "board/main";
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
