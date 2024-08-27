package com.codingjoa.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CategoryService;
import com.codingjoa.service.ImageService;
import com.codingjoa.validator.BoardValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RequestMapping("/board")
@RequiredArgsConstructor
@Controller 
public class BoardController {
	
	private final CategoryService categoryService;
	private final BoardService boardService;
	private final ImageService imageService;
	
	@InitBinder (value = { "writeBoardDto", "modifyBoardDto" })
	protected void initBinderBoard(WebDataBinder binder) {
		binder.addValidators(new BoardValidator(imageService));
	}
	
	@GetMapping
	public String getBoards(Model model) {
		log.info("## getBoards");
		List<Category> boardCategoryList = categoryService.getBoardCategoryList();
		List<List<BoardDetailsDto>> boardList = boardCategoryList
				.stream()
				.map(category -> boardService.getPagedBoard(category.getCategoryCode(), new Criteria(1, 5)))
				.collect(Collectors.toList());
		model.addAttribute("boardCategoryList", boardCategoryList);
		model.addAttribute("boardList", boardList);
		
		return "board/boards";
	}
	
	@GetMapping("/")
	public String getBoard(@BoardCategoryCode @RequestParam int boardCategoryCode, @BoardCri Criteria boardCri, Model model) {
		log.info("## getBoard, boardCategoryCode = {}", boardCategoryCode);
		
		List<BoardDetailsDto> pagedBoard = boardService.getPagedBoard(boardCategoryCode, boardCri);
		
		Pagination pagination = boardService.getPagination(boardCategoryCode, boardCri);
		log.info("\t > pagination = {}", pagination);
		
		Category category = categoryService.getCategory(boardCategoryCode);
		model.addAttribute("boardCri", boardCri);
		model.addAttribute("pagedBoard", pagedBoard);
		model.addAttribute("pagination", pagination);
		model.addAttribute("category", category);
		
		return "board/board";
	}
	
	@GetMapping("/read")
	public String read(@RequestParam int boardIdx, @BoardCri Criteria boardCri, Model model) {
		log.info("## read, boardIdx = {}", boardIdx);
		log.info("\t > boardCri = {}", boardCri);
		
		BoardDetailsDto boardDetails = boardService.getBoardDetails(boardIdx);
		Category category = categoryService.getCategory(boardDetails.getBoardCategoryCode());

		// 쿠키를 이용하여 조회수 중복 방지 추가하기 (https://mighty96.github.io/til/view)
		boardService.updateBoardViews(boardIdx);

		model.addAttribute("boardDetails", boardDetails);
		model.addAttribute("category", category);
		
		return "board/read";
	}
	
	@GetMapping("/write")
	public String write(@BoardCategoryCode @RequestParam int boardCategoryCode, 
			/* @ModelAttribute("writeBoardDto") BoardDto writeBoardDto, */ Model model) {
		log.info("## write, boardCategoryCode = {}", boardCategoryCode);
		BoardDto writeBoardDto = new BoardDto();
		writeBoardDto.setBoardCategoryCode(boardCategoryCode);
		model.addAttribute("writeBoardDto", writeBoardDto);
		model.addAttribute("boardCategoryList", categoryService.getBoardCategoryList());
		
		return "board/write";
	}
	
	@PostMapping("/writeProc")
	public String writeProc(@Validated @ModelAttribute("writeBoardDto") BoardDto writeBoardDto, 
			 BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principal, Model model) throws BindException {
		log.info("## writeProc");
		log.info("\t > {}", writeBoardDto);
		
		if (bindingResult.hasErrors()) {
			log.info("\t > bindingResult hasErrors");
			if (bindingResult.hasFieldErrors("boardCategoryCode") /* || bindingResult.hasFieldErrors("boardIdx") */) {
				throw new BindException(bindingResult);
			}
			model.addAttribute("boardCategoryList", categoryService.getBoardCategoryList());
			return "board/write";
		}
		
		writeBoardDto.setBoardWriterIdx(principal.getMember().getMemberIdx());
		boardService.writeBoard(writeBoardDto); // insertBoard, activateImage
		
		return "redirect:/board/read?boardIdx=" + writeBoardDto.getBoardIdx();
	}
	
	@GetMapping("/modify")
	public String modify(@RequestParam int boardIdx, @AuthenticationPrincipal PrincipalDetails principal, Model model) {
		log.info("## modify, boardIdx = {}", boardIdx);
		BoardDto modifyBoardDto = boardService.getModifyBoard(boardIdx, principal.getMember().getMemberIdx());
		model.addAttribute("modifyBoardDto", modifyBoardDto);
		model.addAttribute("boardCategoryList", categoryService.getBoardCategoryList());
		
		return "board/modify";
	}
	
	@PostMapping("/modifyProc")
	public String modifyProc(@Validated @ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto, 
			BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principal, Model model) throws BindException {
		log.info("## modifyProc");
		log.info("\t > {}", modifyBoardDto);

		if (bindingResult.hasErrors()) {
			log.info("\t > bindingResult hasErrors");
			if (bindingResult.hasFieldErrors("boardCategoryCode") || bindingResult.hasFieldErrors("boardIdx")) {
				throw new BindException(bindingResult);
			}
			model.addAttribute("boardCategoryList", categoryService.getBoardCategoryList());
			return "board/modify";
		}
		
		modifyBoardDto.setBoardWriterIdx(principal.getMember().getMemberIdx());
		boardService.modifyBoard(modifyBoardDto); // updateBoard, modifyBoardImage
		
		return "redirect:/board/read?boardIdx=" + modifyBoardDto.getBoardIdx();
	}
	
	@GetMapping("/deleteProc")
	public String deleteProc(@RequestParam int boardIdx, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## deleteProc, boardIdx = {}", boardIdx);
		// fk_board_image_board --> ON DELETE SET NULL
		// fk_comment_board		--> ON DELETE CASCADE
		int boardCategoryCode = boardService.deleteBoard(boardIdx, principal.getMember().getMemberIdx());
		
		return "redirect:/board/?boardCategoryCode=" + boardCategoryCode;
	}
	
}
