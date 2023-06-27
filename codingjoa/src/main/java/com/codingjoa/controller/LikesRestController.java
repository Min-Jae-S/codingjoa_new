package com.codingjoa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RestController
public class LikesRestController {

	@GetMapping("/boards/{boardIdx}/likes")
	public ResponseEntity<Object> toggleBoardLikes(@PathVariable int boardIdx,
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## toggleBoardLikes, boardIdx = {}", boardIdx);
		
		return ResponseEntity.ok(SuccessResponse.create().message("toggleBoardLikes success"));
	}

	@GetMapping("/comments/{commentIdx}/likes")
	public ResponseEntity<Object> toggleCommentLikes(@PathVariable int commentIdx, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## toggleCommentLikes, commentIdx = {}", commentIdx);
		
		return ResponseEntity.ok(SuccessResponse.create().message("toggleCommentLikes success"));
	}
}
