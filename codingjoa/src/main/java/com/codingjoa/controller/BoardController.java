package com.codingjoa.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.codingjoa.dto.BoardDto;
import com.codingjoa.dto.SearchDto;
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
	
	@Resource(name = "boardValidator")
	private Validator boardValidator;
	
	@InitBinder(value = { "writeBoardDto", "modifyBoardDto" })
	public void initBinderBoard(WebDataBinder binder) {
		binder.addValidators(boardValidator);
	}
	
	@ModelAttribute
	public void categoryList(Model model) {
		model.addAttribute("categoryList", categoryService.findBoardCategoryList());
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
	public String write(@RequestParam int categoryCode, 
						@ModelAttribute("writeBoardDto") BoardDto writeBoardDto, Model model) {
		log.info("categoryCode={}", categoryCode);
		
		writeBoardDto.setBoardCategoryCode(categoryCode);
		log.info("{}", writeBoardDto);
		
		return "board/write";
	}
	
	@PostMapping("/writeProc")
	public String writeProc(@Valid @ModelAttribute("writeBoardDto") BoardDto writeBoardDto, 
							BindingResult bindingResult, Model model) {
		log.info("{}", writeBoardDto);
		
		if (bindingResult.hasErrors()) { // TypeMismatch, objectError.getCodes()[0]
			return "board/write";
		}
		
		boardService.writeBoard(writeBoardDto);
		log.info("After write, {}", writeBoardDto);
		
		boardService.activateTempImage(writeBoardDto);
		
		return "board/write-success";
	}
	
	@GetMapping("/modify")
	public String modify(@ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto, Model model) {
		log.info("{}", modifyBoardDto);
		
		boardService.bindModifyBoard(modifyBoardDto);
		log.info("After binding, {}", modifyBoardDto);
		
		return "board/modify";
	}
	
	@PostMapping("/modifyProc")
	public String modifyProc(@Valid @ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto, 
							 BindingResult bindingResult, Model model) {
		log.info("{}", modifyBoardDto);
		
		if (bindingResult.hasErrors()) {
			return "board/modify";
		}
		
		boardService.modifyBoard(modifyBoardDto);
		
		// uploadIdx: 새로 업로드된 이미지, 이미 존재하는 이미지, 삭제된 이미지
		// 	- 새로 업로드된 이미지, 이미 존재하는 이미지 --> update upload_board_idx  
		// 	- 삭제된 이미지	--> update upload_board_idx null
		// [DB]  uploadIdxList<Integer> : [ 22, 23 ]
		// [NEW] uploadIdxList<Integer> : [ 23. 24 ]
		// 22(삭제된 이미지), 23(존재하는 이미지), 24(새로 업로드된 이미지)
		
		return "board/modify-success";
	}
	
	@GetMapping("/delete")
	public String delete() {
		
		return null;
	}
}
