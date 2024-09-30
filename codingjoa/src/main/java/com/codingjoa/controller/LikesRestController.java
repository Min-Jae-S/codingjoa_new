package com.codingjoa.controller;

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
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.LikesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class LikesRestController {
	
	private final LikesService likesService;
	private final UserDetailsService userDetailsService;

	@PostMapping("/boards/{boardIdx}/likes")
	public ResponseEntity<Object> toggleBoardLikes(@PathVariable int boardIdx, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## toggleBoardLikes, boardIdx = {}", boardIdx);
		boolean isBooardLiked = likesService.toggleBoardLikes(boardIdx, principal.getIdx());
		String code = (isBooardLiked) ? "success.LikeBoard" : "success.UnlikeBoard";
		//resetAuthentication(principal.getMember().getMemberId());
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode(code).data(isBooardLiked).build());
	}
	
	@GetMapping("/boards/{boardIdx}/likes")
	public ResponseEntity<Object> getBoardLikesCnt(@PathVariable int boardIdx) {
		log.info("## getBoardLikesCnt, boardIdx = {}", boardIdx);
		
		int boardLikesCnt = likesService.getBoardLikesCnt(boardIdx);
		log.info("\t > boardLikesCnt = {}", boardLikesCnt);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(boardLikesCnt).build());
	}
	
	@PostMapping("/comments/{commentIdx}/likes")
	public ResponseEntity<Object> toggleCommentLikes(@PathVariable int commentIdx, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## toggleCommentLikes, commentIdx = {}", commentIdx);
		boolean isCommentLiked = likesService.toggleCommentLikes(commentIdx, principal.getIdx());
		String code = (isCommentLiked) ? "success.LikeComment" : "success.UnlikeComment";
		//resetAuthentication(principal.getMember().getMemberId());

		return ResponseEntity.ok(SuccessResponse.builder().messageByCode(code).data(isCommentLiked).build());
	}

	@GetMapping("/comments/{commentIdx}/likes")
	public ResponseEntity<Object> getCommentLikesCnt(@PathVariable int commentIdx) {
		log.info("## getCommentLikesCnt, commentIdx = {}", commentIdx);
		
		int commentLikesCnt = likesService.getCommentLikesCnt(commentIdx);
		log.info("\t > commentLikesCnt = {}", commentLikesCnt);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(commentLikesCnt).build());
	}
	
	@SuppressWarnings("unused")
	private void resetAuthentication(String memberId) {
		log.info("## resetAuthentication");
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		Authentication newAuthentication = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	}
}
