package com.codingjoa.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.ModifyBoardDto;
import com.codingjoa.dto.SearchDto;
import com.codingjoa.dto.WriteBoardDto;
import com.codingjoa.security.dto.UserDetailsDto;
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
	
	@Resource(name = "writeBoardValidator")
	private Validator writeBoardValidator;
	
	@InitBinder("writeBoardDto")
	public void initBinderWriteBoard(WebDataBinder binder) {
		binder.addValidators(writeBoardValidator);
	}
	
	@GetMapping("/all")
	public String all() {
		return "board/all";
	}
	
	@GetMapping("/main")
	public String main(@RequestParam("categoryCode") int categoryCode, 
			@ModelAttribute SearchDto searchDto, Model model) {
		log.info("categoryCode={}, {}", categoryCode, searchDto);
		
		model.addAttribute("category", categoryService.findCategory(categoryCode));
		model.addAttribute("boardDetailsList", boardService.getBoardDetailsList(categoryCode));
		
		return "board/main";
	}
	
	@GetMapping("/read")
	public String read(@RequestParam("boardIdx") int boardIdx, Model model) {
		log.info("boardIdx={}", boardIdx);
		
		boardService.updateBoardViews(boardIdx);
		
		BoardDetailsDto boardDetails = boardService.getBoardDetails(boardIdx);
		model.addAttribute("boardDetails", boardDetails);
		
		int categoryCode = boardDetails.getBoardCategoryCode();
		model.addAttribute("category", categoryService.findCategory(categoryCode));
		
		return "board/read";
	}
	
	@GetMapping("/write")
	public String write(@RequestParam("categoryCode") int categoryCode, 
			@ModelAttribute WriteBoardDto writeBoardDto, @AuthenticationPrincipal UserDetailsDto principal, Model model) {
		log.info("categoryCode={}, {}", categoryCode, writeBoardDto);
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		writeBoardDto.setBoardWriterIdx(boardWriterIdx);
		writeBoardDto.setBoardCategoryCode(categoryCode);
		
		//model.addAttribute("categoryList", categoryService.findCategoryOfSameParent(categoryCode));
		model.addAttribute("categoryList", categoryService.findBoardCategoryList());
		
		return "board/write";
	}
	
	@PostMapping("/writeProc")
	public String writeProc(@ModelAttribute @Valid WriteBoardDto writeBoardDto, 
			BindingResult bindingResult, Model model) {
		log.info("{}", writeBoardDto);
		
		if (bindingResult.hasErrors()) { // TypeMismatch, objectError.getCodes()[0]
			model.addAttribute("categoryList", categoryService.findBoardCategoryList());
			return "board/write";
		}
		
		int boardIdx = boardService.writeBoard(writeBoardDto);
		writeBoardDto.setBoardIdx(boardIdx);
		
		boardService.activateTempImage(writeBoardDto);
		
		return "board/write-success";
	}
	
	@GetMapping("/modify")
	public String modify(@RequestParam("boardIdx") int boardIdx, 
			@ModelAttribute ModifyBoardDto modifyBoardDto, Model model) {
		log.info("boardIdx={}, {}", boardIdx, modifyBoardDto);
		
		boardService.mapModifyBoard(boardIdx, modifyBoardDto);
		log.info("After mapping, {}", modifyBoardDto);
		
		model.addAttribute("categoryList", categoryService.findBoardCategoryList());
		
		return "board/modify";
	}
	
	@PostMapping("/modifyProc")
	public String modifyProc(@ModelAttribute ModifyBoardDto modifyBoardDto, 
			BindingResult bindingResult, Model model) {
		log.info("{}", modifyBoardDto);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("categoryList", categoryService.findBoardCategoryList());
			return "board/modify";
		}
		
		return "board/modify-success";
	}
	
	@GetMapping("/delete")
	public String delete() {
		
		return null;
	}
}
