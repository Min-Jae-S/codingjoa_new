package com.codingjoa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.annotation.CommentCri;
import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// https://velog.io/@yoojkim/Rest-API-RESTful%ED%95%98%EA%B2%8C-URL-%EC%84%A4%EA%B3%84%ED%95%98%EA%B8%B0
// REST : 자원(HTTP URI), 행위(HTTP Method), 표현(HTTP Message Payload)

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CommentRestController {
	
	private final CommentService commentService;
	
	// https://stackoverflow.com/questions/31680960/spring-initbinder-on-requestbody
	// @InitBinder doesn't work with @RequestBody, it can work with @ModelAttribute Annotation.
	//binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));

	@GetMapping("/boards/{boardId}/comments")
	public ResponseEntity<Object> getPagedComments(@PathVariable long boardId,
			@CommentCri CommentCriteria commentCri, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## getPagedComments, boardId = {}", boardId);
		log.info("\t > commentCri = {}", commentCri);
		
		Long userId = (principal == null) ? null : principal.getId();
		log.info("\t > userId = {}", userId);

		List<CommentDetailsDto> pagedComments = commentService.getPagedComments(boardId, commentCri, userId);
		
		Pagination pagination = commentService.getPagination(boardId, commentCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedComments", pagedComments);
		data.put("pagination", pagination);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}
	
	@PostMapping("/comments")
	public ResponseEntity<Object> write(@Valid @RequestBody CommentDto commentDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## write");

		commentDto.setUserId(principal.getId());
		commentDto.setStatus(true);
		log.info("\t > commentDto = {}", commentDto);
		
		commentService.saveComment(commentDto);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.comment.write")
				.build());
	}
	
	@PatchMapping(value = { "/comments/", "/comments/{commentId}" })
	public ResponseEntity<Object> modify(@PathVariable long commentId, 
			@Valid @RequestBody CommentDto commentDto, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## modify, commentId = {}", commentId);
		
		commentDto.setId(commentId);
		commentDto.setUserId(principal.getId());
		log.info("\t > commentDto = {}", commentDto);

		commentService.updateComment(commentDto);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.comment.modify")
				.build());
	}
	
	@DeleteMapping(value = { "/comments/", "/comments/{commentId}" })
	public ResponseEntity<Object> delete(@PathVariable long commentId, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## delete, commentId = {}", commentId);
		commentService.deleteComment(commentId, principal.getId()); // update status
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.comment.delete")
				.build());
	}
	
}
