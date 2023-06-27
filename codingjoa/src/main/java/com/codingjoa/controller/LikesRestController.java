package com.codingjoa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RestController
public class LikesRestController {

	@GetMapping("/board/{boardIdx}/likes")
	public ResponseEntity<Object> toggleBoardLikes(@PathVariable int boardIdx) {
		log.info("## toggleBoardLikes, boardIdx = {}", boardIdx);
		
		return null;
	}

	@GetMapping("/comments/{commentIdx}/likes")
	public ResponseEntity<Object> toggleCommentLikes(@PathVariable int commentIdx) {
		log.info("## toggleCommentLikes, commentIdx = {}", commentIdx);
		
		return null;
	}
}
