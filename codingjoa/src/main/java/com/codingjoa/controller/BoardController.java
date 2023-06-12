package com.codingjoa.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
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
@RequestMapping("/board")
@Controller @Validated
public class BoardController {
	
	@Autowired 
	private CategoryService categoryService;

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private UploadService uploadService;
	
	@Resource(name = "boardValidator")
	private Validator boardValidator;

	@InitBinder (value = { "writeBoardDto", "modifyBoardDto" })
	protected void initBinderBoard(WebDataBinder binder) {
		log.info("## initBinderBoard");
		log.info("\t > target = {} / {}", binder.getObjectName(), binder.getTarget());
		binder.addValidators(boardValidator);
	}
	
	@GetMapping
	public String getAllBoards(Model model) {
		log.info("## getAllBoards");
		
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
	public String getBoard(@BoardCategoryCode @RequestParam int boardCategoryCode, 
			@BoardCri Criteria boardCri, Model model) {
		log.info("## getBoard, boardCategoryCode = {}", boardCategoryCode);
		log.info("\t > boardCri = {}", boardCri);

		Criteria newBoardCri = boardService.makeNewBoardCri(boardCri);
		log.info("\t > new boardCri = {}", newBoardCri);
		log.info("\t > keyword regexp = {}", newBoardCri.getKeywordRegexp());
		
		List<BoardDetailsDto> board = boardService.getPagedBoard(boardCategoryCode, newBoardCri);
		model.addAttribute("board", board);
		
		Pagination pagination = boardService.getPagination(boardCategoryCode, newBoardCri);
		model.addAttribute("pagination", pagination);
		log.info("\t > {}", pagination);
		
		Category category = categoryService.findCategory(boardCategoryCode);
		model.addAttribute("category", category);
		
		return "board/board";
	}
	
	@GetMapping("/read")
	public String read(@RequestParam int boardIdx, @BoardCri Criteria boardCri, Model model) {
		log.info("## read, boardIdx = {}", boardIdx);
		log.info("\t > {}", boardCri);
		
		BoardDetailsDto boardDetails = boardService.getBoardDetails(boardIdx);
		model.addAttribute("boardDetails", boardDetails);
		log.info("\t > {}", boardDetails);
		
		Category category = categoryService.findCategory(boardDetails.getBoardCategoryCode());
		model.addAttribute("category", category);

		// 쿠키를 이용하여 조회수 중복 방지 추가하기
		// https://mighty96.github.io/til/view
		boardService.updateBoardViews(boardIdx);
		
		return "board/read";
	}
	
	@GetMapping("/write")
	public String write(@BoardCategoryCode @RequestParam int boardCategoryCode, Model model) {
		log.info("## write, boardCategoryCode = {}", boardCategoryCode);
		
		BoardDto writeBoardDto = new BoardDto();
		writeBoardDto.setBoardCategoryCode(boardCategoryCode);
		model.addAttribute("writeBoardDto", writeBoardDto);
		model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
		
		return "board/write";
	}
	
	@PostMapping("/writeProc")
	public String writeProc(@Validated @ModelAttribute("writeBoardDto") BoardDto writeBoardDto, 
			 BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal, Model model) 
					 throws BindException {
		log.info("## writeProc");
		log.info("\t > {}", writeBoardDto);
		
		if (bindingResult.hasErrors()) {
			bindingResult.getFieldErrors().forEach(fieldError -> {
				log.info("\t > {} / {}", fieldError.getField(), fieldError.getCodes()[0]);
			});
			
			if (bindingResult.hasFieldErrors("boardCategoryCode")) {
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
		log.info("## modify, boardIdx = {}", boardIdx);
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		log.info("\t > boardWriterIdx = {}", boardWriterIdx);
		
		BoardDto modifyBoardDto = boardService.getModifyBoard(boardIdx, boardWriterIdx);
		model.addAttribute("modifyBoardDto", modifyBoardDto);
		model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
		
		return "board/modify";
	}
	
	@PostMapping("/modifyProc")
	public String modifyProc(@Validated @ModelAttribute("modifyBoardDto") BoardDto modifyBoardDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal, Model model) 
					throws BindException {
		log.info("## modifyProc");
		log.info("\t > {}", modifyBoardDto);

		if (bindingResult.hasErrors()) {
			bindingResult.getFieldErrors().forEach(fieldError -> {
				log.info("\t > {} / {}", fieldError.getField(), fieldError.getCodes()[0]);
			});
			
			if (bindingResult.hasFieldErrors("boardCategoryCode")) {
				throw new BindException(bindingResult);
			}
			
			model.addAttribute("boardCategoryList", categoryService.findBoardCategoryList());
			return "board/modify";
		}
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		log.info("\t > boardWriterIdx = {}", boardWriterIdx);
		
		modifyBoardDto.setBoardWriterIdx(boardWriterIdx);
		boardService.modifyBoard(modifyBoardDto); // updateBoard + updateUpload
		uploadService.modifyUpload(modifyBoardDto);
		
		return "redirect:/board/read?boardIdx=" + modifyBoardDto.getBoardIdx();
	}
	
	@GetMapping("/deleteProc")
	public String deleteProc(@ModelAttribute("deleteBoardDto") BoardDto deleteBoardDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## deleteProc");
		log.info("\t > {}", deleteBoardDto);
		
		int boardWriterIdx = principal.getMember().getMemberIdx();
		log.info("\t > boardWriterIdx = {}", boardWriterIdx);
		
		// ON DELETE CASCADE, ON DELETE SET NULL
		deleteBoardDto.setBoardWriterIdx(boardWriterIdx);
		boardService.deleteBoard(deleteBoardDto);
		
		// UriComponentsBuilder.newInstance().queryParam("boardCategoryCode", boardCategoryCode).toUriString()
		return "redirect:/board/?boardCategoryCode=" + deleteBoardDto.getBoardCategoryCode();
	}
	
}
