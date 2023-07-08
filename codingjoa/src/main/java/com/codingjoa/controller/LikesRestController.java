package com.codingjoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
	
	@Autowired
	private UserDetailsService userDetailsService;

	@PostMapping("/boards/{boardIdx}/likes")
	public ResponseEntity<Object> toggleBoardLikes(@PathVariable int boardIdx,
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## toggleBoardLikes, boardIdx = {}", boardIdx);
		
		BoardLikesDto boardLikesDto = new BoardLikesDto();
		boardLikesDto.setBoardIdx(boardIdx);
		boardLikesDto.setMemberIdx(principal.getMember().getMemberIdx());
		Integer boardLikesIdx = likesService.toggleBoardLikes(boardLikesDto);
		log.info("\t > {}", boardLikesIdx == null ? "Insert boardLikes" : "Delete boardLikes");
		
		resetAuthentication(principal.getMember().getMemberId());
		
		SuccessResponse response = SuccessResponse.create();
		if (boardLikesIdx == null) {
			response.data("ON").code("success.InsertBoardLikes");
		} else {
			response.data("OFF").code("success.DeleteBoardLikes");
		}
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/boards/{boardIdx}/likes")
	public ResponseEntity<Object> getBoardLikesCnt(@PathVariable int boardIdx) {
		log.info("## getBoardLikesCnt, boardIdx = {}", boardIdx);
		
		int boardLikesCnt = likesService.getBoardLikesCnt(boardIdx);
		log.info("\t > boardLikesCnt = {}", boardLikesCnt);
		
		return ResponseEntity.ok(SuccessResponse.create().data(boardLikesCnt));
	}
	
	@PostMapping("/comments/{commentIdx}/likes")
	public ResponseEntity<Object> toggleCommentLikes(@PathVariable int commentIdx, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## toggleCommentLikes, commentIdx = {}", commentIdx);
		
		CommentLikesDto commentLikesDto = new CommentLikesDto();
		commentLikesDto.setCommentIdx(commentIdx);
		commentLikesDto.setMemberIdx(principal.getMember().getMemberIdx());
		Integer commentLikesIdx = likesService.toggleCommentLikes(commentLikesDto);
		log.info("\t > {}", commentLikesIdx == null ? "Insert commentLikes" : "Delete commentLikes");
		
		resetAuthentication(principal.getMember().getMemberId());
		
		SuccessResponse response = SuccessResponse.create();
		if (commentLikesIdx == null) {
			response.data("ON").code("success.InsertCommentLikes");
		} else {
			response.data("OFF").code("success.DeleteCommentLikes");
		}
		
		return ResponseEntity.ok(response);
	}

	@GetMapping("/comments/{commentIdx}/likes")
	public ResponseEntity<Object> getCommentLikesCnt(@PathVariable int commentIdx) {
		log.info("## getCommentLikesCnt, commentIdx = {}", commentIdx);
		
		int commentLikesCnt = likesService.getCommentLikesCnt(commentIdx);
		log.info("\t > commentLikesCnt = {}", commentLikesCnt);
		
		return ResponseEntity.ok(SuccessResponse.create().data(commentLikesCnt));
	}
	
	private void resetAuthentication(String memberId) {
		log.info("## resetAuthentication");
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		Authentication newAuthentication = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	}
}
