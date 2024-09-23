package com.codingjoa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.annotation.CommentCri;
import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// https://velog.io/@yoojkim/Rest-API-RESTful%ED%95%98%EA%B2%8C-URL-%EC%84%A4%EA%B3%84%ED%95%98%EA%B8%B0
// REST : 자원(HTTP URI), 행위(HTTP Method), 표현(HTTP Message Payload)

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CommentRestController {
	
	private final CommentService commentService;
	
	// https://stackoverflow.com/questions/31680960/spring-initbinder-on-requestbody
	// @InitBinder doesn't work with @RequestBody, it can work with @ModelAttribute Annotation.
	//binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));

	@GetMapping("/boards/{boardIdx}/comments")
	public ResponseEntity<Object> getPagedComment(@PathVariable int boardIdx,
			@CommentCri CommentCriteria commentCri, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## getPagedComment, boardIdx = {}", boardIdx);
		log.info("\t > commentCri = {}", commentCri);
		
		Integer memberIdx = (principal == null) ? null : principal.getIdx();
		log.info("\t > memberIdx = {}", memberIdx);

		List<CommentDetailsDto> pagedComment = commentService.getPagedComment(boardIdx, commentCri, memberIdx);
		log.info("\t > pagedComment = {}", pagedComment);
		
		Pagination pagination = commentService.getPagination(boardIdx, commentCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedComment", pagedComment);
		data.put("pagination", pagination);

		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}
	
	@GetMapping(value = { "/comments/", "/comments/{commentIdx}" })
	public ResponseEntity<Object> getModifyComment(@PathVariable int commentIdx, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## getModifyComment, commentIdx = {}", commentIdx);
		CommentDetailsDto commentDetails = commentService.getModifyComment(commentIdx, principal.getIdx());
		return ResponseEntity.ok(SuccessResponse.builder().data(commentDetails).build());
	}
	
	@PostMapping("/comments")
	public ResponseEntity<Object> writeComment(@Valid @RequestBody CommentDto writeCommentDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## writeComment");
		log.info("\t > writeCommentDto = {}", writeCommentDto);
		writeCommentDto.setMemberIdx(principal.getIdx());
		writeCommentDto.setCommentUse(true);
		commentService.saveComment(writeCommentDto);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.WriteComment").build());
	}
	
	@PatchMapping(value = { "/comments/", "/comments/{commentIdx}" })
	public ResponseEntity<Object> modifyComment(@PathVariable int commentIdx, 
			@Valid @RequestBody CommentDto modifyCommentDto, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## modifyComment, commentIdx = {}", commentIdx);
		log.info("\t > modifyCommentDto = {}", modifyCommentDto);
		modifyCommentDto.setCommentIdx(commentIdx);
		modifyCommentDto.setMemberIdx(principal.getIdx());
		commentService.updateComment(modifyCommentDto);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.UpdateComment").build());
	}
	
	@DeleteMapping(value = { "/comments/", "/comments/{commentIdx}" })
	public ResponseEntity<Object> deleteComment(@PathVariable int commentIdx, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## deleteComment, commentIdx = {}", commentIdx);
		commentService.deleteComment(commentIdx, principal.getIdx()); // update commentUse
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.DeleteComment").build());
	}
	
}
