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

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "Like API")
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class LikeRestController {
	
	private final LikeService likeService;

	@PostMapping("/boards/{boardId}/likes")
	public ResponseEntity<Object> toggleBoardLike(@PathVariable long boardId, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## toggleBoardLike, boardId = {}", boardId);
		
		boolean isBoardLiked = likeService.toggleBoardLike(boardId, principal.getId());
		String code = (isBoardLiked) ? "success.like.board" : "success.dislike.board";
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode(code)
				.data(isBoardLiked)
				.build());
	}
	
	@GetMapping("/boards/{boardId}/likes")
	public ResponseEntity<Object> getBoardLikeCnt(@PathVariable long boardId) {
		log.info("## getBoardLikeCnt, boardId = {}", boardId);
		
		int boardLikeCnt = likeService.getBoardLikeCnt(boardId);
		log.info("\t > boardLikeCnt = {}", boardLikeCnt);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(boardLikeCnt).build());
	}
	
	@PostMapping("/comments/{commentId}/likes")
	public ResponseEntity<Object> toggleCommentLike(@PathVariable long commentId, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## toggleCommentLike, commentId = {}", commentId);
		
		boolean isCommentLiked = likeService.toggleCommentLike(commentId, principal.getId());
		String code = (isCommentLiked) ? "success.like.comment" : "success.dislike.comment";
		//resetAuthentication(principal.getMember().getMemberId());

		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode(code)
				.data(isCommentLiked)
				.build());
	}

	@GetMapping("/comments/{commentId}/likes")
	public ResponseEntity<Object> getCommentLikeCnt(@PathVariable long commentId) {
		log.info("## getCommentLikeCnt, commentId = {}", commentId);
		
		int commentLikeCnt = likeService.getCommentLikeCnt(commentId);
		log.info("\t > commentLikeCnt = {}", commentLikeCnt);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(commentLikeCnt).build());
	}
	
}
