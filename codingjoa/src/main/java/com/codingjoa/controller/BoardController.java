package com.codingjoa.controller;

import java.util.ArrayList;
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
import org.springframework.web.util.UriComponentsBuilder;

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
	public void boardCategoryList(Model model) {
		model.addAttribute("categoryList", categoryService.findBoardCategoryList());
		model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
	}
	
	@GetMapping("/all")
	public String all(Model model) {
		ArrayList<List<BoardDetailsDto>> boardListContainer = new ArrayList<List<BoardDetailsDto>>();
		
		categoryService.findBoardCategoryList().forEach(category -> {
			Criteria cri = new Criteria(category.getCategoryCode(), 1, 5, null, null);
			List<BoardDetailsDto> recentBoardList = boardService.getPagedBoardList(cri);
			boardListContainer.add(recentBoardList);
		});
		
		model.addAttribute("boardListContainer", boardListContainer);
		
		return "board/all";
	}
	
	@GetMapping("/main")
	public String main(@Cri Criteria cri, Model model) {
		log.info("{}", cri);
		
		model.addAttribute("cri", cri);
		model.addAttribute("boardName", categoryService.findCategoryName(cri.getBoardCategoryCode()));

		List<BoardDetailsDto> boardList = boardService.getPagedBoardList(cri);
		model.addAttribute("boardList", boardList);
		
		Pagination pagination = boardService.getPagination(cri);
		model.addAttribute("pagination", pagination);
		
		log.info("{}", pagination);
		
		return "board/main";
	}
	
	@GetMapping("/read")
	public String read(@RequestParam("boardIdx") int boardIdx, @Cri Criteria cri, Model model) {
		log.info("boardIdx={}", boardIdx);
		log.info("{}", cri);
		
		model.addAttribute("cri", cri);
		model.addAttribute("boardName", categoryService.findCategoryName(cri.getBoardCategoryCode()));
		
		BoardDetailsDto boardDetails = boardService.getBoardDetails(boardIdx);
		model.addAttribute("boardDetails", boardDetails);
		
		// 쿠키를 이용하여 조회수 중복 방지 추가하기 (https://mighty96.github.io/til/view)
		boardService.updateBoardViews(boardIdx);
		
		return "board/read";
	}
	
	@GetMapping("/write")
	public String write(@ModelAttribute("writeBoardDto") BoardDto writeBoardDto) {
		log.info("{}", writeBoardDto);
		
		return "board/write";
	}
	
	@PostMapping("/writeProc")
	public String writeProc(@Valid @ModelAttribute("writeBoardDto") BoardDto writeBoardDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal) {
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
		
		return "redirect:/board/read" + UriComponentsBuilder.newInstance()
											.queryParam("boardIdx", boardIdx)
											.queryParam("boardCategoryCode", writeBoardDto.getBoardCategoryCode())
											.toUriString();
	}
	
	@GetMapping("/modify")
	public String modify(@ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto) {
		log.info("{}", modifyBoardDto);
		
		boardService.bindModifyBoard(modifyBoardDto);
		log.info("After bind, {}", modifyBoardDto);
		
		return "board/modify";
	}
	
	@PostMapping("/modifyProc")
	public String modifyProc(@Valid @ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal) {
		log.info("{}", modifyBoardDto);
		
		if (bindingResult.hasErrors()) {
			return "board/modify";
		}
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		modifyBoardDto.setBoardWriterIdx(boardWriterIdx);
		
		boardService.modifyBoard(modifyBoardDto);
		boardService.modifyUpload(modifyBoardDto);
		
		return "redirect:/board/read" + UriComponentsBuilder.newInstance()
											.queryParam("boardIdx", modifyBoardDto.getBoardIdx())
											.queryParam("boardCategoryCode", modifyBoardDto.getBoardCategoryCode())
											.toUriString();
	}
	
	@GetMapping("/deleteProc")
	public String deleteProc(@RequestParam("boardIdx") int boardIdx) {
		log.info("boardIdx={}", boardIdx);
		
		int boardCategoryCode = boardService.getBoardCategoryCode(boardIdx);
		
		// ON DELETE CASCADE, ON DELETE SET NULL
		boardService.deleteBoard(boardIdx);
		
		return "redirect:/board/main" + UriComponentsBuilder.newInstance()
											.queryParam("boardCategoryCode", boardCategoryCode)
											.toUriString();
	}
}
