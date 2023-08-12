package com.codingjoa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
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
import com.codingjoa.validator.BoardValidator;

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
	
	@InitBinder (value = { "writeBoardDto", "modifyBoardDto" })
	protected void initBinderBoard(WebDataBinder binder) {
		binder.addValidators(new BoardValidator(uploadService));
	}
	
	@GetMapping
	public String getBoards(Model model) {
		log.info("## getBoards");
		
		List<Category> boardCategoryList = categoryService.findBoardCategoryList();
		model.addAttribute("boardCategoryList", boardCategoryList);
		
		Criteria boardCri = new Criteria(1, 5);
		List<List<BoardDetailsDto>> boardList = boardCategoryList
				.stream()
				.map(category -> boardService.getPagedBoard(category.getCategoryCode(), boardCri))
				.collect(Collectors.toList());
		model.addAttribute("boardList", boardList);
		
		return "board/boards";
	}
	
	@GetMapping("/")
	public String getBoard(@BoardCategoryCode @RequestParam int boardCategoryCode, 
			@BoardCri Criteria boardCri, Model model) {
		log.info("## getBoard");
		log.info("\t > boardCategoryCode = {}", boardCategoryCode);
		log.info("\t > boardCri = {}", boardCri);

		Criteria newBoardCri = boardService.makeNewBoardCri(boardCri);
		log.info("\t > new boardCri = {}", newBoardCri);
		log.info("\t > keyword regexp = {}", newBoardCri.getKeywordRegexp());
		
		List<BoardDetailsDto> board = boardService.getPagedBoard(boardCategoryCode, newBoardCri);
		model.addAttribute("board", board);
		log.info("\t > paged board = {}", board);
		
		Pagination pagination = boardService.getPagination(boardCategoryCode, newBoardCri);
		model.addAttribute("pagination", pagination);
		log.info("\t > {}", pagination);
		
		Category category = categoryService.findCategory(boardCategoryCode);
		model.addAttribute("category", category);
		
		return "board/board";
	}
	
	@GetMapping("/read")
	public String read(@RequestParam int boardIdx, @BoardCri Criteria boardCri, Model model) {
		log.info("## read");
		log.info("\t > boardIdx = {}", boardIdx);
		log.info("\t > {}", boardCri);
		
		BoardDetailsDto boardDetails = boardService.getBoardDetails(boardIdx);
		model.addAttribute("boardDetails", boardDetails);
		
		Category category = categoryService.findCategory(boardDetails.getBoardCategoryCode());
		model.addAttribute("category", category);

		// 쿠키를 이용하여 조회수 중복 방지 추가하기
		// https://mighty96.github.io/til/view
		boardService.updateBoardViews(boardIdx);
		
		return "board/read";
	}
	
	@GetMapping("/write")
	public String write(@BoardCategoryCode @RequestParam int boardCategoryCode, Model model) {
		log.info("## write");
		log.info("\t > boardCategoryCode = {}", boardCategoryCode);
		
		BoardDto writeBoardDto = new BoardDto();
		writeBoardDto.setBoardCategoryCode(boardCategoryCode);
		model.addAttribute("writeBoardDto", writeBoardDto);
		model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
		
		return "board/write";
	}
	
	@PostMapping("/writeProc")
	public String writeProc(@Validated @ModelAttribute("writeBoardDto") BoardDto writeBoardDto, 
			 BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal, Model model) throws BindException {
		log.info("## writeProc");
		log.info("\t > {}", writeBoardDto);
		
		if (bindingResult.hasErrors()) {
			if (bindingResult.hasFieldErrors("boardCategoryCode") || bindingResult.hasFieldErrors("boardIdx")) {
				throw new BindException(bindingResult);
			}
			model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
			return "board/write";
		}
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		writeBoardDto.setBoardWriterIdx(boardWriterIdx);
		boardService.writeBoard(writeBoardDto); // insertBoard + activateImage
		
		return "redirect:/board/read?boardIdx=" + writeBoardDto.getBoardIdx();
	}
	
	@GetMapping("/modify")
	public String modify(@RequestParam int boardIdx, @AuthenticationPrincipal UserDetailsDto principal, Model model) {
		log.info("## modify");
		log.info("\t > boardIdx = {}", boardIdx);
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		BoardDto modifyBoardDto = boardService.getModifyBoard(boardIdx, boardWriterIdx);
		model.addAttribute("modifyBoardDto", modifyBoardDto);
		model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
		
		return "board/modify";
	}
	
	@PostMapping("/modifyProc")
	public String modifyProc(@Validated @ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal, Model model) throws BindException {
		log.info("## modifyProc");
		log.info("\t > {}", modifyBoardDto);

		if (bindingResult.hasErrors()) {
			if (bindingResult.hasFieldErrors("boardCategoryCode") || bindingResult.hasFieldErrors("boardIdx")) {
				throw new BindException(bindingResult);
			}
			model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
			return "board/modify";
		}
		
		modifyBoardDto.setBoardWriterIdx(principal.getMember().getMemberIdx());
		boardService.modifyBoard(modifyBoardDto); // updateBoard + deactivateImage + activateImage  
		
		return "redirect:/board/read?boardIdx=" + modifyBoardDto.getBoardIdx();
	}
	
	@GetMapping("/deleteProc")
	public String deleteProc(@RequestParam int boardIdx, @AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## deleteProc");
		log.info("\t > boardIdx = {}", boardIdx);
		
		BoardDto deleteBoardDto = new BoardDto();
		deleteBoardDto.setBoardIdx(boardIdx);
		deleteBoardDto.setBoardWriterIdx(principal.getMember().getMemberIdx());
		
		// ON DELETE CASCADE, ON DELETE SET NULL
		boardService.deleteBoard(deleteBoardDto);
		
		// UriComponentsBuilder.newInstance().queryParam("boardCategoryCode", boardCategoryCode).toUriString()
		return "redirect:/board/?boardCategoryCode=" + deleteBoardDto.getBoardCategoryCode();
	}
	
}
