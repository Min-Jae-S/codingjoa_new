package com.codingjoa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.LikeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class LikeRestController {
	
	private final LikeService likeService;

	@PostMapping("/boards/{boardId}/likes")
	public ResponseEntity<Object> toggleBoardLikes(@PathVariable int boardId, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## toggleBoardLikes, boardId = {}", boardId);
		
		boolean isBooardLiked = likeService.toggleBoardLikes(boardId, principal.getId());
		String code = (isBooardLiked) ? "success.LikeBoard" : "success.UnlikeBoard";
		//resetAuthentication(principal.getMember().getMemberId());
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode(code)
				.data(isBooardLiked)
				.build());
	}
	
	@GetMapping("/boards/{boardId}/likes")
	public ResponseEntity<Object> getBoardLikesCnt(@PathVariable int boardId) {
		log.info("## getBoardLikesCnt, boardId = {}", boardId);
		
		int boardLikesCnt = likeService.getBoardLikesCnt(boardId);
		log.info("\t > boardLikesCnt = {}", boardLikesCnt);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(boardLikesCnt).build());
	}
	
	@PostMapping("/replies/{replyId}/likes")
	public ResponseEntity<Object> toggleReplyLikes(@PathVariable int replyId, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## toggleReplyLikes, replyId = {}", replyId);
		
		boolean isCommentLiked = likeService.toggleCommentLikes(replyId, principal.getId());
		String code = (isCommentLiked) ? "success.LikeComment" : "success.UnlikeComment";
		//resetAuthentication(principal.getMember().getMemberId());

		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode(code)
				.data(isCommentLiked)
				.build());
	}

	@GetMapping("/comments/{commentIdx}/likes")
	public ResponseEntity<Object> getCommentLikesCnt(@PathVariable int commentIdx) {
		log.info("## getCommentLikesCnt, commentIdx = {}", commentIdx);
		
		int commentLikesCnt = likeService.getCommentLikesCnt(commentIdx);
		log.info("\t > commentLikesCnt = {}", commentLikesCnt);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(commentLikesCnt).build());
	}
	
}
