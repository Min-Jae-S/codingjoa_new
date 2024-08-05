package com.codingjoa.controller;

import java.util.Map;

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

import com.codingjoa.dto.SuccessResponse;
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
	public ResponseEntity<Object> toggleBoardLikes(@PathVariable int boardIdx, @AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## toggleBoardLikes, boardIdx = {}", boardIdx);
		boolean isBoardLiked = likesService.toggleBoardLikes(boardIdx, principal.getMember().getMemberIdx());
		String code = (isBoardLiked) ? "success.LikeBoard" : "success.UnlikeBoard";
		resetAuthentication(principal.getMember().getMemberId());
		
		return ResponseEntity.ok(SuccessResponse
				.builder()
				.messageByCode(code)
				.data(Map.of("isBoardLiked", isBoardLiked))
				.build());
	}
	
	@GetMapping("/boards/{boardIdx}/likes")
	public ResponseEntity<Object> getBoardLikesCnt(@PathVariable int boardIdx) {
		log.info("## getBoardLikesCnt, boardIdx = {}", boardIdx);
		
		int boardLikesCnt = likesService.getBoardLikesCnt(boardIdx);
		log.info("\t > boardLikesCnt = {}", boardLikesCnt);
		
		return ResponseEntity.ok(SuccessResponse
				.builder()
				.data(Map.of("boardLikesCnt", boardLikesCnt))
				.build());
	}
	
	@PostMapping("/comments/{commentIdx}/likes")
	public ResponseEntity<Object> toggleCommentLikes(@PathVariable int commentIdx, @AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## toggleCommentLikes, commentIdx = {}", commentIdx);
		boolean isCommentLiked = likesService.toggleCommentLikes(commentIdx, principal.getMember().getMemberIdx());
		String code = (isCommentLiked) ? "success.LikeComment" : "success.UnlikeComment";
		resetAuthentication(principal.getMember().getMemberId());

		return ResponseEntity.ok(SuccessResponse
				.builder()
				.messageByCode(code)
				.data(Map.of("isCommentLiked", isCommentLiked))
				.build());
	}

	@GetMapping("/comments/{commentIdx}/likes")
	public ResponseEntity<Object> getCommentLikesCnt(@PathVariable int commentIdx) {
		log.info("## getCommentLikesCnt, commentIdx = {}", commentIdx);
		
		int commentLikesCnt = likesService.getCommentLikesCnt(commentIdx);
		log.info("\t > commentLikesCnt = {}", commentLikesCnt);
		
		return ResponseEntity.ok(SuccessResponse
				.builder()
				.data(Map.of("commentLikesCnt", commentLikesCnt))
				.build());
	}
	
	private void resetAuthentication(String memberId) {
		log.info("## resetAuthentication");
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		Authentication newAuthentication = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	}
}
