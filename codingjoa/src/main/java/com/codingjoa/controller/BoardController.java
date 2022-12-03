package com.codingjoa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codingjoa.entity.Category;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/board")
@Controller
public class BoardController {
	
	@Autowired 
	private CategoryService categoryService;

	@Autowired
	private BoardService boardService;
	
	@GetMapping("/all")
	public String all() {
		return "board/all";
	}
	
	@GetMapping("/main")
	public String main(@RequestParam("categoryCode") int categoryCode, Model model) {
		log.info("categoryCode = {}", categoryCode);
		
		model.addAttribute("category", categoryService.findCategory(categoryCode));
		
		return "board/main";
	}
	
	@GetMapping("/read")
	public String read() {
		return "board/read";
	}
	
	@GetMapping("/write")
	public String write(@RequestParam("categoryCode") int categoryCode, Model model) {
		log.info("categoryCode = {}", categoryCode);
		
		List<Category> list = categoryService.findCategoryOfSameParent(categoryCode);
		log.info("{}", list);
		
		model.addAttribute("list", list);
		
		return "board/write";
	}
	
	@PostMapping("/writeProc")
	public String writeProc() {
		return "board/write-success";
	}
}
