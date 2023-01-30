package com.codingjoa.controller;

import java.util.List;

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

import com.codingjoa.annotation.Cri;
import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.pagination.Criteria;
import com.codingjoa.pagination.Pagination;
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
	public String main(@RequestParam("categoryCode") int categoryCode, @Cri Criteria cri, Model model) {
		log.info("categoryCode={}, {}", categoryCode, cri);
		log.info("query={}", cri.getQuery());
		
		model.addAttribute("cri", cri);
		model.addAttribute("category", categoryService.findCategory(categoryCode));

		List<BoardDetailsDto> boardList = boardService.getPagedBoardList(categoryCode, cri);
		model.addAttribute("boardList", boardList);
		
		Pagination pagination = boardService.getPagination(categoryCode, cri);
		model.addAttribute("pagination", pagination);
		log.info("{}", pagination);
		
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
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal, Model model) {
		log.info("{}", writeBoardDto);
		
		if (bindingResult.hasErrors()) {
			return "board/write";
		}
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		writeBoardDto.setBoardWriterIdx(boardWriterIdx);
		
		int boardIdx = boardService.writeBoard(writeBoardDto);
		log.info("boardIdx={}", boardIdx);
		
		writeBoardDto.setBoardIdx(boardIdx);
		boardService.activateImage(writeBoardDto);
		
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
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal, Model model) {
		log.info("{}", modifyBoardDto);
		
		if (bindingResult.hasErrors()) {
			return "board/modify";
		}
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		modifyBoardDto.setBoardWriterIdx(boardWriterIdx);
		
		boardService.modifyBoard(modifyBoardDto);
		boardService.modifyUpload(modifyBoardDto);
		
		return "board/modify-success";
	}
	
	@GetMapping("/deleteProc")
	public String deleteProc(@RequestParam("boardIdx") int boardIdx, Model model) {
		log.info("boardIdx={}", boardIdx);
		
		return "board/delete-success";
	}
}
