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
	public ResponseEntity<Object> toggleBoardLike(@PathVariable long boardId, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## toggleBoardLike, boardId = {}", boardId);
		
		boolean isBoardLiked = likeService.toggleBoardLike(boardId, principal.getId());
		String code = (isBoardLiked) ? "success.LikeBoard" : "success.UnlikeBoard";
		//resetAuthentication(principal.getMember().getMemberId());
		
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
	
	@PostMapping("/replies/{replyId}/likes")
	public ResponseEntity<Object> toggleReplyLike(@PathVariable long replyId, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## toggleReplyLike, replyId = {}", replyId);
		
		boolean isReplyLiked = likeService.toggleReplyLike(replyId, principal.getId());
		String code = (isReplyLiked) ? "success.LikeReply" : "success.UnlikeReply";
		//resetAuthentication(principal.getMember().getMemberId());

		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode(code)
				.data(isReplyLiked)
				.build());
	}

	@GetMapping("/replies/{replyId}/likes")
	public ResponseEntity<Object> getReplyLikeCnt(@PathVariable long replyId) {
		log.info("## getReplyLikeCnt, replyId = {}", replyId);
		
		int replyLikeCnt = likeService.getReplyLikeCnt(replyId);
		log.info("\t > replyLikeCnt = {}", replyLikeCnt);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(replyLikeCnt).build());
	}
	
}
