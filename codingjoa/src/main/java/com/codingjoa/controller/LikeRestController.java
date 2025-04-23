package com.codingjoa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.annotation.PrivateApi;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.LikeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "Like API")
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class LikeRestController {
	
	private final LikeService likeService;

	@PrivateApi
	@ApiOperation(value = "게시글 좋아요 토글", notes = "게시글에 좋아요 또는 좋아요 취소를 수행한다. (인증 필요)")
	@PostMapping("/boards/{boardId}/likes")
	public ResponseEntity<Object> toggleBoardLike(@PathVariable Long boardId, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## toggleBoardLike, boardId = {}", boardId);
		
		boolean isBoardLiked = likeService.toggleBoardLike(boardId, principal.getId());
		String code = (isBoardLiked) ? "success.like.board" : "success.dislike.board";
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode(code)
				.data(isBoardLiked)
				.build());
	}
	
	@ApiOperation(value = "게시글 좋아요 수 조회", notes = "특정 게시글의 총 좋아요 수를 반환한다.")
	@GetMapping("/boards/{boardId}/likes")
	public ResponseEntity<Object> getBoardLikeCnt(@PathVariable Long boardId) {
		log.info("## getBoardLikeCnt, boardId = {}", boardId);
		
		int boardLikeCnt = likeService.getBoardLikeCnt(boardId);
		log.info("\t > boardLikeCnt = {}", boardLikeCnt);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(boardLikeCnt).build());
	}
	
	@PrivateApi
	@ApiOperation(value = "댓글 좋아요 토글", notes = "댓글에 좋아요 또는 좋아요 취소를 수행한다. (인증 필요)")
	@PostMapping("/comments/{commentId}/likes")
	public ResponseEntity<Object> toggleCommentLike(@PathVariable Long commentId, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## toggleCommentLike, commentId = {}", commentId);
		
		boolean isCommentLiked = likeService.toggleCommentLike(commentId, principal.getId());
		String code = (isCommentLiked) ? "success.like.comment" : "success.dislike.comment";

		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode(code)
				.data(isCommentLiked)
				.build());
	}

	@ApiOperation(value = "댓글 좋아요 수 조회", notes = "특정 댈글의 총 좋아요 수를 반환한다.")
	@GetMapping("/comments/{commentId}/likes")
	public ResponseEntity<Object> getCommentLikeCnt(@PathVariable Long commentId) {
		log.info("## getCommentLikeCnt, commentId = {}", commentId);
		
		int commentLikeCnt = likeService.getCommentLikeCnt(commentId);
		log.info("\t > commentLikeCnt = {}", commentLikeCnt);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(commentLikeCnt).build());
	}
	
}
