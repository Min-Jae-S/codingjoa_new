package com.codingjoa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codingjoa.dto.BoardDto;
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
	public String write(@RequestParam("categoryCode") int categoryCode, 
						@ModelAttribute BoardDto boardDto, Model model) {
		log.info("categoryCode = {}", categoryCode);
		log.info("{}", boardDto);
		
		model.addAttribute("categoryCode", categoryCode);
		model.addAttribute("categoryList", categoryService.findCategoryOfSameParent(categoryCode));
		
		return "board/write";
	}
	
	@PostMapping("/writeProc")
	public String writeProc(@ModelAttribute @Valid BoardDto boardDto, 
							BindingResult bindingResult, Model model) {
		log.info("{}", boardDto);
		
		if(bindingResult.hasErrors()) {
			int categoryCode = boardDto.getBoardCategoryCode();
			model.addAttribute("categoryList", categoryService.findCategoryOfSameParent(categoryCode));
			
			return "board/write";
		}
		
		return "board/write-success";
	}
}
