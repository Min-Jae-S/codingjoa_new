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
import com.codingjoa.entity.Board;
import com.codingjoa.entity.Category;
import com.codingjoa.pagination.BoardCriteria;
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
	public String getBoards(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
		log.info("## getBoards");
		
		Integer memberIdx = (principal == null) ? null : principal.getIdx();
		log.info("\t > memberIdx = {}", memberIdx);
		
		List<Category> boardCategoryList = categoryService.getBoardCategoryList();
		List<List<BoardDetailsDto>> boardList = boardCategoryList
				.stream()
				.map(category -> boardService.getPagedBoard(category.getCategoryCode(), new BoardCriteria(1, 5), memberIdx))
				.collect(Collectors.toList());
		model.addAttribute("boardCategoryList", boardCategoryList);
		model.addAttribute("boardList", boardList);
		
		return "board/boards";
	}
	
	@GetMapping("/")
	public String getPagedBoard(@BoardCategoryCode @RequestParam int boardCategoryCode, @BoardCri BoardCriteria boardCri,
			@AuthenticationPrincipal PrincipalDetails principal, Model model) {
		log.info("## getPagedBoard, boardCategoryCode = {}", boardCategoryCode);
		log.info("\t > boardCri = {}", boardCri);
	
		Integer memberIdx = (principal == null) ? null : principal.getIdx();
		log.info("\t > memberIdx = {}", memberIdx);
		
		List<BoardDetailsDto> pagedBoard = boardService.getPagedBoard(boardCategoryCode, boardCri, memberIdx);
		
		Pagination pagination = boardService.getPagination(boardCategoryCode, boardCri);
		log.info("\t > board pagination = {}", pagination);
		
		Category category = categoryService.getCategory(boardCategoryCode);
		model.addAttribute("pagedBoard", pagedBoard);
		model.addAttribute("pagination", pagination);
		model.addAttribute("category", category);
		
		return "board/board";
	}
	
	@GetMapping("/read")
	public String read(@RequestParam int boardIdx, @BoardCri BoardCriteria boardCri,
			@AuthenticationPrincipal PrincipalDetails principal, Model model) {
		log.info("## read, boardIdx = {}", boardIdx);
		log.info("\t > boardCri = {}", boardCri);
		
		Integer memberIdx = (principal == null) ? null : principal.getIdx();
		log.info("\t > memberIdx = {}", memberIdx);
		
		BoardDetailsDto boardDetails = boardService.getBoardDetails(boardIdx, memberIdx);
		log.info("\t > boardDetails = {}", boardDetails);
		
		Category category = categoryService.getCategory(boardDetails.getBoardCategoryCode());

		boardService.updateBoardViews(boardIdx); // 쿠키를 이용하여 조회수 중복 방지 추가하기 (https://mighty96.github.io/til/view)

		model.addAttribute("boardDetails", boardDetails);
		model.addAttribute("category", category);
		
		return "board/read";
	}
	
	@GetMapping("/write")
	public String write(@BoardCategoryCode @RequestParam int boardCategoryCode, Model model) {
		log.info("## write, boardCategoryCode = {}", boardCategoryCode);
		BoardDto writeBoardDto = new BoardDto();
		writeBoardDto.setBoardCategoryCode(boardCategoryCode);
		model.addAttribute("writeBoardDto", writeBoardDto);
		model.addAttribute("boardCategoryList", categoryService.getBoardCategoryList());
		
		return "board/write";
	}
	
	@PostMapping("/write")
	public String write(@Validated @ModelAttribute("writeBoardDto") BoardDto writeBoardDto, 
			 BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principal, Model model) throws BindException {
		log.info("## write");
		log.info("\t > writeBoardDto = {}", writeBoardDto);
		
		if (bindingResult.hasErrors()) {
			List<String> errorFields = bindingResult.getFieldErrors()
					.stream()
					.map(fieldError -> fieldError.getField())
					.collect(Collectors.toList());
			log.info("\t > bindingResult hasErrors = {}", errorFields);
			
			if (bindingResult.hasFieldErrors("boardCategoryCode")) {
				throw new BindException(bindingResult);
			}
			
			model.addAttribute("boardCategoryList", categoryService.getBoardCategoryList());
			return "board/write";
		}
		
		writeBoardDto.setBoardWriterIdx(principal.getIdx());
		Board savedBoard = boardService.saveBoard(writeBoardDto); // insertBoard & activateImage
		
		return "redirect:/board/read?boardIdx=" + savedBoard.getBoardIdx();
	}
	
	@GetMapping("/modify")
	public String modify(@RequestParam int boardIdx, @AuthenticationPrincipal PrincipalDetails principal, Model model) {
		log.info("## modify, boardIdx = {}", boardIdx);
		BoardDto modifyBoardDto = boardService.getModifyBoard(boardIdx, principal.getIdx());
		model.addAttribute("modifyBoardDto", modifyBoardDto);
		model.addAttribute("boardCategoryList", categoryService.getBoardCategoryList());
		
		return "board/modify";
	}
	
	@PostMapping("/modify")
	public String modify(@Validated @ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto, 
			BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principal, Model model) throws BindException {
		log.info("## modify");
		log.info("\t > modifyBoardDto = {}", modifyBoardDto);

		if (bindingResult.hasErrors()) {
			List<String> errorFields = bindingResult.getFieldErrors()
					.stream()
					.map(fieldError -> fieldError.getField())
					.collect(Collectors.toList());
			log.info("\t > bindingResult hasErrors = {}", errorFields);
			
			if (bindingResult.hasFieldErrors("boardCategoryCode") || bindingResult.hasFieldErrors("boardIdx")) {
				throw new BindException(bindingResult);
			}
			
			model.addAttribute("boardCategoryList", categoryService.getBoardCategoryList());
			return "board/modify";
		}
		
		modifyBoardDto.setBoardWriterIdx(principal.getIdx());
		Board modifiedBoard = boardService.modifyBoard(modifyBoardDto); // updateBoard, deactivateBoardImage, activateImage
		
		return "redirect:/board/read?boardIdx=" + modifiedBoard.getBoardIdx();
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam int boardIdx, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## delete, bordIdx = {}", boardIdx);

		// fk_board_image_board --> ON DELETE SET NULL
		// fk_comment_board		--> ON DELETE CASCADE
		Board deletedBoard = boardService.deleteBoard(boardIdx, principal.getIdx());
		
		return "redirect:/board/?boardCategoryCode=" + deletedBoard.getBoardCategoryCode();
	}
	
}
