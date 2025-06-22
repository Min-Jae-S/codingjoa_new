package com.codingjoa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.entity.Category;
import com.codingjoa.pagination.BoardCriteria;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.BoardService;
import com.codingjoa.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

	private final BoardService boardService;
	private final CategoryService categoryService;
	
	@GetMapping("/")
	public String main(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
		log.info("## main");
		BoardCriteria boardCri = BoardCriteria.create();
		Long userId = obtainUserId(principal);
		log.info("\t > userId = {}", userId);
		
		List<Category> boardCategories = categoryService.getBoardCategories();
		List<List<BoardDetailsDto>> boards = boardCategories
				.stream()
				.map(category -> boardService.getPagedBoards(category.getCode(), boardCri, userId))
				.collect(Collectors.toList());
		model.addAttribute("boardCategories", boardCategories);
		model.addAttribute("boards", boards);
		
		return "main";
	}
	
	private Long obtainUserId(PrincipalDetails principal) {
		return (principal == null) ? null : principal.getId();
	}
	
}
