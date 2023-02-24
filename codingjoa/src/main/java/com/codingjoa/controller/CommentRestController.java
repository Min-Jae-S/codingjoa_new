package com.codingjoa.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.error.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.CommentService;

import lombok.extern.slf4j.Slf4j;

// https://velog.io/@yoojkim/Rest-API-RESTful%ED%95%98%EA%B2%8C-URL-%EC%84%A4%EA%B3%84%ED%95%98%EA%B8%B0
// 자원(HTTP URI), 행위(HTTP Method), 표현(HTTP Message Payload)

@Slf4j
@RestController
public class CommentRestController {
	
	@Autowired
	private CommentService commentService;

	@PostMapping("/comment")
	public ResponseEntity<Object> writeComment(@Valid @RequestBody CommentDto commentDto,
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal) {
		log.info("{}", commentDto);
		
		if (bindingResult.hasErrors()) {
			// ...
		}
		
		int commentWriterIdx = principal.getMember().getMemberIdx();
		commentDto.setCommentWriterIdx(commentWriterIdx);
		commentDto.setCommentUse(true);
		
		commentService.writeComment(commentDto);
		
		return null;
	}
	
	@GetMapping("/comment/board/{boardIdx}")
	public ResponseEntity<Object> getBoardComment(@PathVariable int boardIdx) {
		log.info("boardIdx={}", boardIdx);
		
		List<CommentDetailsDto> commentList = commentService.getPagedComment();
		
		//return null;
		return ResponseEntity.ok(SuccessResponse.create().data(commentList));
	}
	
}
