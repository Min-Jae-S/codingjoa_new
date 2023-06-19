package com.codingjoa.controller;

import java.util.List;

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
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.response.SuccessResponse;
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
	// @InitBinder doesn't work with @RequesBody, it can work with @ModelAttribute Annotation.
	//binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));

	@GetMapping("/boards/{boardIdx}/comments")
	public ResponseEntity<Object> getCommentList(@PathVariable int boardIdx, @CommentCri CommentCriteria commentCri) {
		log.info("## getCommentList, boardIdx = {}", boardIdx);
		log.info("\t > {}", commentCri);
		
		List<CommentDetailsDto> commentList = commentService.getPagedComment(boardIdx, commentCri);
		
		return ResponseEntity.ok(SuccessResponse.create().data(commentList));
	}
	
	@GetMapping(value = { "/comments", "/comments/{commentIdx}" })
	public ResponseEntity<Object> getComment(@PathVariable int commentIdx,
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## getComment, commentIdx = {}", commentIdx);
		
		CommentDetailsDto commentDetails = 
				commentService.getCommentDetails(commentIdx, principal.getMember().getMemberIdx());
		
		return ResponseEntity.ok(SuccessResponse.create().data(commentDetails));
	}
	
	@PostMapping("/comments")
	public ResponseEntity<Object> writeComment(@Valid @RequestBody CommentDto writeCommentDto,
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## writeComment");
		log.info("\t > {}", writeCommentDto);
		
		writeCommentDto.setCommentWriterIdx(principal.getMember().getMemberIdx());
		writeCommentDto.setCommentUse(true);
		commentService.writeComment(writeCommentDto);
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.writeComment"));
	}
	
	@PatchMapping(value = { "/comments", "/comments/{commentIdx}" })
	public ResponseEntity<Object> modifyComment(@PathVariable int commentIdx) {
		log.info("## modifyComment, commentIdx = {}", commentIdx);
		// update ...
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.updateComment"));
	}
	
	@DeleteMapping(value = { "/comments", "/comments/{commentIdx}" })
	public ResponseEntity<Object> deleteComment(@PathVariable int commentIdx,
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## deleteComment, commentIdx = {}", commentIdx);
		
		CommentDto deleteCommentDto = new CommentDto();
		deleteCommentDto.setCommentIdx(commentIdx);
		deleteCommentDto.setCommentUse(false);
		//deleteCommentDto.setCommentWriterIdx(principal.getMember().getMemberIdx());
		deleteCommentDto.setCommentWriterIdx(1);
		commentService.deleteComment(deleteCommentDto); // update commentUse(true -> false)
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.deleteComment"));
	}
	
}
