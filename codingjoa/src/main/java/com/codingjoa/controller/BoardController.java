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

import com.codingjoa.annotation.BoardCri;
import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Category;
import com.codingjoa.pagination.BoardCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board")
@Slf4j
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

//	@ModelAttribute
//	public void boardCategoryList(Model model) {
//		model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
//	}
	
	@GetMapping("/all")
	public String getAllBoard(Model model) {
		List<Category> boardCategoryList = categoryService.findBoardCategoryList();
		model.addAttribute("boardCategoryList", boardCategoryList);
		
		ArrayList<List<BoardDetailsDto>> boardList = new ArrayList<List<BoardDetailsDto>>();
		boardCategoryList.forEach(category -> {
			BoardCriteria boardCri = new BoardCriteria(1, 5);
			List<BoardDetailsDto> board = boardService.getPagedBoard(boardCri);
			boardList.add(board);
		});
		model.addAttribute("boardList", boardList);
		
		return "board/all";
	}
	
	@GetMapping("/main")
	public String getBoard(@RequestParam("boardCategorycode") int boardCategorycode, 
			@BoardCri BoardCriteria boardCri, Model model) {
		log.info("boardCategorycode = ", boardCategorycode);
		log.info("{}", boardCri);
		
		model.addAttribute("boardCri", boardCri);

		BoardCriteria newCri = boardService.makeNewCri(boardCri);
		log.info("newCri = {}", newCri);
		
		List<BoardDetailsDto> board = boardService.getPagedBoard(newCri);
		model.addAttribute("board", board);
		
		Pagination pagination = boardService.getPagination(newCri);
		model.addAttribute("pagination", pagination);
		log.info("{}", pagination);
		
//		String boardName = categoryService.findCategoryName(boardCategorycode);
//		model.addAttribute("boardName", boardName);
		
		return "board/main";
	}
	
	@GetMapping("/read")
	public String read(@RequestParam("boardIdx") int boardIdx, 
			@BoardCri BoardCriteria boardCri, Model model) {
		log.info("boardIdx = {}", boardIdx);
		log.info("{}", boardCri);
		
		model.addAttribute("boardCri", boardCri);

		BoardDetailsDto boardDetails = boardService.getBoardDetails(boardIdx);
		model.addAttribute("boardDetails", boardDetails);
		log.info("boardDetails = {}", boardDetails);
		
		String boardName = categoryService.findCategoryName(boardDetails.getBoardCategoryCode());
		model.addAttribute("boardName", boardName);

		// 쿠키를 이용하여 조회수 중복 방지 추가하기 (https://mighty96.github.io/til/view)
		boardService.updateBoardViews(boardIdx);
		
		return "board/read";
	}
	
	@GetMapping("/write")
	public String write(@ModelAttribute("writeBoardDto") BoardDto writeBoardDto, Model model) {
		log.info("{}", writeBoardDto);
		
		model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
		
		return "board/write";
	}
	
	@PostMapping("/writeProc")
	public String writeProc(@Valid @ModelAttribute("writeBoardDto") BoardDto writeBoardDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal, Model model) {
		log.info("{}", writeBoardDto);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
			return "board/write";
		}
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		writeBoardDto.setBoardWriterIdx(boardWriterIdx);
		
		int boardIdx = boardService.writeBoard(writeBoardDto);
		log.info("boardIdx = {}", boardIdx);
		
		writeBoardDto.setBoardIdx(boardIdx);
		boardService.activateImage(writeBoardDto);
		
		return "redirect:/board/read" + UriComponentsBuilder.newInstance()
											.queryParam("boardIdx", boardIdx)
											.toUriString();
	}
	
	@GetMapping("/modify")
	public String modify(@ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto, 
			Model model) {
		log.info("{}", modifyBoardDto);
		
		boardService.bindModifyBoard(modifyBoardDto);
		log.info("After bind, {}", modifyBoardDto);
		
		model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
		
		return "board/modify";
	}
	
	@PostMapping("/modifyProc")
	public String modifyProc(@Valid @ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal, Model model) {
		log.info("{}", modifyBoardDto);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
			return "board/modify";
		}
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		modifyBoardDto.setBoardWriterIdx(boardWriterIdx);
		
		boardService.modifyBoard(modifyBoardDto);
		boardService.modifyUpload(modifyBoardDto);
		
		return "redirect:/board/read" + UriComponentsBuilder.newInstance()
											.queryParam("boardIdx", modifyBoardDto.getBoardIdx())
											.toUriString();
	}
	
	@GetMapping("/deleteProc")
	public String deleteProc(@RequestParam("boardIdx") int boardIdx) {
		log.info("boardIdx = {}", boardIdx);
		
		int boardCategoryCode = boardService.getBoardCategoryCode(boardIdx);
		
		// ON DELETE CASCADE
		// ON DELETE SET NULL
		boardService.deleteBoard(boardIdx);
		
		return "redirect:/board/main" + UriComponentsBuilder.newInstance()
											.queryParam("boardCategoryCode", boardCategoryCode)
											.toUriString();
	}
}
