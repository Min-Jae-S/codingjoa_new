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
import com.codingjoa.resolver.BoardCriResolver;
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
	private final BoardCriResolver boardCriResolver;
	
	@InitBinder (value = { "writeBoardDto", "modifyBoardDto" })
	protected void initBinderBoard(WebDataBinder binder) {
		binder.addValidators(new BoardValidator(imageService));
	}
	
	@GetMapping
	public String getBoards(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
		log.info("## getBoards");
		
		Integer userId = (principal == null) ? null : principal.getId();
		log.info("\t > userId = {}", userId);
		
		List<Category> boardCategoryList = categoryService.getBoardCategoryList();
		List<List<BoardDetailsDto>> boardList = boardCategoryList
				.stream()
				.map(category -> boardService.getPagedBoards(category.getCode(), BoardCriteria.create(), userId))
				.collect(Collectors.toList());
		model.addAttribute("boardCategoryList", boardCategoryList);
		model.addAttribute("boardList", boardList);
		
		return "board/boards";
	}
	
	@GetMapping("/")
	public String getPagedBoards(@BoardCategoryCode @RequestParam int categoryCode, 
			@BoardCri BoardCriteria boardCri, @AuthenticationPrincipal PrincipalDetails principal, Model model) {
		log.info("## getPagedBoards");
		log.info("\t > categoryCode = {}, boardCri = {}", categoryCode, boardCri);
	
		Integer userId = (principal == null) ? null : principal.getId();
		log.info("\t > userId = {}", userId);
		
		List<BoardDetailsDto> pagedBoards = boardService.getPagedBoards(categoryCode, boardCri, userId);
		
		Pagination pagination = boardService.getPagination(categoryCode, boardCri);
		log.info("\t > pagination = {}", pagination);
		
		Category category = categoryService.getCategory(categoryCode);
		
		model.addAttribute("boardCri", boardCri);
		model.addAttribute("pagedBoards", pagedBoards);
		model.addAttribute("pagination", pagination);
		model.addAttribute("category", category);
		model.addAttribute("options", boardCriResolver.getOptions());
		
		return "board/board";
	}
	
	@GetMapping("/read")
	public String read(@RequestParam int boardId, @BoardCri BoardCriteria boardCri,
			@AuthenticationPrincipal PrincipalDetails principal, Model model) {
		log.info("## read");
		log.info("\t > boardId = {}, boardCri = {}", boardId, boardCri);
		
		Integer userId = (principal == null) ? null : principal.getId();
		log.info("\t > userId = {}", userId);
		
		BoardDetailsDto boardDetails = boardService.getBoardDetails(boardId, userId);
		log.info("\t > boardDetails = {}", boardDetails);
		
		Category category = categoryService.getCategory(boardDetails.getCategoryCode());

		// ** 쿠키를 이용하여 조회수 중복 방지 추가하기 (https://mighty96.github.io/til/view)
		boardService.updateBoardView(boardId); 

		model.addAttribute("boardDetails", boardDetails);
		model.addAttribute("category", category);
		
		return "board/read";
	}
	
	@GetMapping("/write")
	public String write(@BoardCategoryCode @RequestParam int categoryCode, Model model) {
		log.info("## write, categoryCode = {}", categoryCode);
		BoardDto writeBoardDto = new BoardDto();
		writeBoardDto.setCategoryCode(categoryCode);
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
			
			if (bindingResult.hasFieldErrors("categoryCode")) {
				throw new BindException(bindingResult);
			}
			
			model.addAttribute("boardCategoryList", categoryService.getBoardCategoryList());
			return "board/write";
		}
		
		writeBoardDto.setUserId(principal.getId());
		Board savedBoard = boardService.saveBoard(writeBoardDto); // insertBoard & activateImage
		
		return "redirect:/board/read?id=" + savedBoard.getId();
	}
	
	@GetMapping("/modify")
	public String modify(@RequestParam int id, @AuthenticationPrincipal PrincipalDetails principal, Model model) {
		log.info("## modify, id = {}", id);
		BoardDto modifyBoardDto = boardService.getModifyBoard(id, principal.getId());
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
		
		modifyBoardDto.setUserId(principal.getId());
		Board modifiedBoard = boardService.modifyBoard(modifyBoardDto); // updateBoard, deactivateImage, activateImage
		
		return "redirect:/board/read?id=" + modifiedBoard.getId();
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam int id, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## delete, id = {}", id);

		// fk_boardimage_board 	--> ON DELETE SET NULL
		// fk_reply_board		--> ON DELETE CASCADE
		Board deletedBoard = boardService.deleteBoard(id, principal.getId());
		
		return "redirect:/board/?categoryCode=" + deletedBoard.getCategoryCode();
	}
	
}
