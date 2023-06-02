package com.codingjoa.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codingjoa.annotation.BoardCategoryCode;
import com.codingjoa.annotation.BoardCri;
import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardDto;
import com.codingjoa.entity.Category;
import com.codingjoa.pagination.Criteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CategoryService;
import com.codingjoa.service.UploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RequestMapping("/board")
@Controller
public class BoardController {
	
	@Autowired 
	private CategoryService categoryService;

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private UploadService uploadService;
	
	@Resource(name = "boardValidator")
	private Validator boardValidator;

	@InitBinder(value = { "writeBoardDto", "modifyBoardDto" })
	protected void initBinderBoard(WebDataBinder binder) {
		log.info("-------- initBinderBoard --------");
		binder.addValidators(boardValidator);
	}
	
	@GetMapping
	public String getAllBoards(Model model) {
		List<Category> boardCategoryList = categoryService.findBoardCategoryList();
		model.addAttribute("boardCategoryList", boardCategoryList);
		
		Criteria boardCri = new Criteria(1, 5);
		List<List<BoardDetailsDto>> boardList = boardCategoryList
				.stream()
				.map(category -> boardService.getPagedBoard(category.getCategoryCode(), boardCri))
				.collect(Collectors.toList());
		model.addAttribute("boardList", boardList);
		
		return "board/all-boards";
	}
	
	@GetMapping("/")
	public String getBoard(@BoardCategoryCode @RequestParam("boardCategoryCode") int boardCategoryCode, 
			@BoardCri Criteria boardCri, Model model) {
		log.info("boardCategoryCode = {}", boardCategoryCode);
		log.info("boardCri = {}", boardCri);

		Criteria newBoardCri = boardService.makeNewBoardCri(boardCri);
		log.info("newBoardCri = {}", newBoardCri);
		log.info("keyword regexp = {}", newBoardCri.getKeywordRegexp());
		
		List<BoardDetailsDto> board = boardService.getPagedBoard(boardCategoryCode, newBoardCri);
		model.addAttribute("board", board);
		
		Pagination pagination = boardService.getPagination(boardCategoryCode, newBoardCri);
		model.addAttribute("pagination", pagination);
		log.info("pagination = {}", pagination);
		
		Category category = categoryService.findCategory(boardCategoryCode);
		model.addAttribute("category", category);
		model.addAttribute("boardCri", boardCri);
		
		return "board/board";
	}
	
	@GetMapping("/read")
	public String read(@RequestParam("boardIdx") int boardIdx, @BoardCri Criteria boardCri, Model model) {
		log.info("boardIdx = {}", boardIdx);
		log.info("boardCri = {}", boardCri);
		
		BoardDetailsDto boardDetails = boardService.getBoardDetails(boardIdx);
		model.addAttribute("boardDetails", boardDetails);
		log.info("boardDetails = {}", boardDetails);
		
		Category category = categoryService.findCategory(boardDetails.getBoardCategoryCode());
		model.addAttribute("category", category);
		model.addAttribute("boardCri", boardCri);

		// 쿠키를 이용하여 조회수 중복 방지 추가하기
		// https://mighty96.github.io/til/view
		boardService.updateBoardViews(boardIdx);
		
		return "board/read";
		
	}
	
	@GetMapping("/write")
	public String write(@ModelAttribute("writeBoardDto") BoardDto writeBoardDto, Model model) {
		log.info("writeBoardDto = {}", writeBoardDto);
		model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
		
		return "board/write";
	}
	
	@PostMapping("/writeProc")
	public String writeProc(@Validated @ModelAttribute("writeBoardDto") BoardDto writeBoardDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal, Model model) {
		log.info("writeBoardDto = {}", writeBoardDto);
		
		if (bindingResult.hasErrors()) {
			bindingResult.getFieldErrors().forEach(fieldError -> {
				log.info("field = {}", fieldError.getField());
				log.info("code = {}", fieldError.getCodes()[0]); 
			});
			model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
			
			return "board/write";
		}
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		log.info("boardWriterIdx = {}", boardWriterIdx);
		
		writeBoardDto.setBoardWriterIdx(boardWriterIdx);
		boardService.writeBoard(writeBoardDto); // insertBoard + activateImage
		
		return "redirect:/board/read?boardIdx=" + writeBoardDto.getBoardIdx();
	}
	
	@GetMapping("/modify")
	public String modify(@ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto, 
			@AuthenticationPrincipal UserDetailsDto principal, Model model) {
		log.info("modifyBoardDto = {}", modifyBoardDto);
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		log.info("boardWriterIdx = {}", boardWriterIdx);
		
		modifyBoardDto.setBoardWriterIdx(boardWriterIdx);
		boardService.bindModifyBoard(modifyBoardDto);
		
		model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
		
		return "board/modify";
	}
	
	@PostMapping("/modifyProc")
	public String modifyProc(@Validated @ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal, Model model) {
		log.info("modifyBoardDto = {}", modifyBoardDto);
		
		if (bindingResult.hasErrors()) {
			bindingResult.getFieldErrors().forEach(fieldError -> {
				log.info("field = {}", fieldError.getField());
				log.info("code = {}", fieldError.getCodes()[0]); 
			});
			model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
			
			return "board/modify";
		}
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		log.info("boardWriterIdx = {}", boardWriterIdx);
		
		modifyBoardDto.setBoardWriterIdx(boardWriterIdx);
		boardService.modifyBoard(modifyBoardDto); // updateBoard + updateUpload
		
		uploadService.modifyUpload(modifyBoardDto);
		
		return "redirect:/board/read?boardIdx=" + modifyBoardDto.getBoardIdx();
	}
	
	@GetMapping("/deleteProc")
	public String deleteProc(@ModelAttribute("deleteBoardDto") BoardDto deleteBoardDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("deleteBoardDto = {}", deleteBoardDto);
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		log.info("boardWriterIdx = {}", boardWriterIdx);
		
		// ON DELETE CASCADE, ON DELETE SET NULL
		deleteBoardDto.setBoardWriterIdx(boardWriterIdx);
		int boardCategoryCode = boardService.deleteBoard(deleteBoardDto);
		
		// UriComponentsBuilder.newInstance().queryParam("boardCategoryCode", boardCategoryCode).toUriString()
		return "redirect:/board/?boardCategoryCode=" + boardCategoryCode;
	}
	
}
