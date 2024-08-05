package com.codingjoa.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.CommentService;

import lombok.extern.slf4j.Slf4j;

// https://velog.io/@yoojkim/Rest-API-RESTful%ED%95%98%EA%B2%8C-URL-%EC%84%A4%EA%B3%84%ED%95%98%EA%B8%B0
// REST : 자원(HTTP URI), 행위(HTTP Method), 표현(HTTP Message Payload)

@Slf4j
@RequestMapping("/api")
@RestController
public class CommentRestController {
	
	@Autowired
	private CommentService commentService;
	
	// https://stackoverflow.com/questions/31680960/spring-initbinder-on-requestbody
	// @InitBinder doesn't work with @RequestBody, it can work with @ModelAttribute Annotation.
	//binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));

	@GetMapping("/boards/{commentBoardIdx}/comments")
	public ResponseEntity<Object> getCommentList(@PathVariable int commentBoardIdx,
			@CommentCri CommentCriteria commentCri, @AuthenticationPrincipal UserDetailsDto princiapl) {
		log.info("## getCommentList, commentBoardIdx = {}", commentBoardIdx);
		log.info("\t > commentCri = {}", commentCri);

		List<CommentDetailsDto> commentList = commentService.getPagedComment(commentBoardIdx, commentCri);
		Pagination pagination = commentService.getPagination(commentBoardIdx, commentCri);
		log.info("\t > pagination = {}", pagination);

		List<Integer> myCommentLikes = (princiapl == null) ? Collections.emptyList() : princiapl.getMyCommentLikes();
		log.info("\t > myCommentLikes = {}", myCommentLikes);
		
		return ResponseEntity.ok(SuccessResponse
				.builder()
				.data(Map.of("commentList", commentList, "myCommentLikes", myCommentLikes, "pagination", pagination))
				.build());
	}
	
	@GetMapping(value = { "/comments/", "/comments/{commentIdx}" })
	public ResponseEntity<Object> getModifyComment(@PathVariable int commentIdx, @AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## getModifyComment, commentIdx = {}", commentIdx);
		CommentDetailsDto commentDetails = 
				commentService.getModifyComment(commentIdx, principal.getMember().getMemberIdx());
		
		return ResponseEntity.ok(SuccessResponse
				.builder()
				.data(Map.of("commentDetails", commentDetails))
				.build());
	}
	
	@PostMapping("/comments")
	public ResponseEntity<Object> writeComment(@Valid @RequestBody CommentDto writeCommentDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## writeComment");
		log.info("\t > {}", writeCommentDto);
		writeCommentDto.setCommentWriterIdx(principal.getMember().getMemberIdx());
		writeCommentDto.setCommentUse(true);
		commentService.writeComment(writeCommentDto);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.WriteComment").build());
	}
	
	@PatchMapping(value = { "/comments/", "/comments/{commentIdx}" })
	public ResponseEntity<Object> modifyComment(@PathVariable int commentIdx, 
			@Valid @RequestBody CommentDto modifyCommentDto, @AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## modifyComment, commentIdx = {}", commentIdx);
		log.info("\t > {}", modifyCommentDto);
		modifyCommentDto.setCommentIdx(commentIdx);
		modifyCommentDto.setCommentWriterIdx(principal.getMember().getMemberIdx());
		commentService.modifyComment(modifyCommentDto);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.UpdateComment").build());
	}
	
	@DeleteMapping(value = { "/comments/", "/comments/{commentIdx}" })
	public ResponseEntity<Object> deleteComment(@PathVariable int commentIdx, @AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## deleteComment, commentIdx = {}", commentIdx);
		commentService.deleteComment(commentIdx, principal.getMember().getMemberIdx()); // update commentUse
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.DeleteComment").build());
	}
	
}
