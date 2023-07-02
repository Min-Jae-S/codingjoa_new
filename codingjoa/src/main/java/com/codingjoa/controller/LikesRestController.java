package com.codingjoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.BoardLikesDto;
import com.codingjoa.dto.CommentLikesDto;
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.LikesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RestController
public class LikesRestController {
	
	@Autowired
	private LikesService likesService;

	@PostMapping("/boards/{boardIdx}/likes")
	public ResponseEntity<Object> toggleBoardLikes(@PathVariable Integer boardIdx,
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## toggleBoardLikes, boardIdx = {}", boardIdx);
		
		BoardLikesDto boardLikesDto = new BoardLikesDto();
		boardLikesDto.setBoardIdx(boardIdx);
		boardLikesDto.setMemberIdx(principal.getMember().getMemberIdx());
		Integer boardLikesIdx = likesService.toggleBoardLikes(boardLikesDto);
		log.info("\t > {}", boardLikesIdx == null ? "Insert boardLikes" : "Delete boardLikes");

		SuccessResponse response = SuccessResponse.create();
		if (boardLikesIdx == null) {
			return ResponseEntity.ok(response.data("UP").code("success.InsertBoardLikes"));
		} else {
			return ResponseEntity.ok(response.data("DOWN").code("success.DeleteBoardLikes"));
		}
	}
	
	@GetMapping("/boards/{boardIdx}/likes")
	public ResponseEntity<Object> getBoardLikesCnt(@PathVariable Integer boardIdx) {
		log.info("## getBoardLikesCnt, boardIdx = {}", boardIdx);
		int boardLikesCnt = likesService.getBoardLikesCnt(boardIdx);
		
		return ResponseEntity.ok(SuccessResponse.create().data(boardLikesCnt));
	}
	
	@PostMapping("/comments/{commentIdx}/likes")
	public ResponseEntity<Object> toggleCommentLikes(@PathVariable Integer commentIdx, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## toggleCommentLikes, commentIdx = {}", commentIdx);
		
		CommentLikesDto commentLikesDto = new CommentLikesDto();
		commentLikesDto.setCommentIdx(commentIdx);
		commentLikesDto.setMemberIdx(principal.getMember().getMemberIdx());
		Integer commentLikesIdx = likesService.toggleCommentLikes(commentLikesDto);
		log.info("\t > {}", commentLikesIdx == null ? "Insert commentLikes" : "Delete commentLikes");
		
		SuccessResponse response = SuccessResponse.create();
		if (commentLikesIdx == null) {
			return ResponseEntity.ok(response.data("UP").code("success.InsertCommentLikes"));
		} else {
			return ResponseEntity.ok(response.data("DOWN").code("success.DeleteCommentLikes"));
		}
	}

	@GetMapping("/comments/{commentIdx}/likes")
	public ResponseEntity<Object> getCommentLikesCnt(@PathVariable Integer commentIdx) {
		log.info("## getCommentLikesCnt, commentIdx = {}", commentIdx);
		int commentLikesCnt = likesService.getCommentLikesCnt(commentIdx);
		
		return ResponseEntity.ok(SuccessResponse.create().data(commentLikesCnt));
	}
}
