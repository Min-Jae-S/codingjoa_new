package com.codingjoa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.annotation.CommentCri;
import com.codingjoa.annotation.PrivateApi;
import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.CommentService;
import com.codingjoa.validator.CommentValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// https://velog.io/@yoojkim/Rest-API-RESTful%ED%95%98%EA%B2%8C-URL-%EC%84%A4%EA%B3%84%ED%95%98%EA%B8%B0
// REST : 자원(HTTP URI), 행위(HTTP Method), 표현(HTTP Message Payload)

@Api(tags = "Comment API")
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CommentRestController {
	
	private final CommentService commentService;
	
	// https://stackoverflow.com/questions/31680960/spring-initbinder-on-requestbody
	// @InitBinder doesn't work with @RequestBody, it can work with @ModelAttribute Annotation.
	
	@InitBinder("commentDto")
	public void InitBinderEmail(WebDataBinder binder) {
		binder.addValidators(new CommentValidator());
	}
	
	@ApiOperation(value = "댓글 목록 조회", notes = "게시판에 해당하는 댓글 목록과 pagination을 조회한다.")
	@GetMapping("/boards/{boardId}/comments")
	public ResponseEntity<Object> getPagedComments(@PathVariable Long boardId,
			@CommentCri CommentCriteria commentCri, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## getPagedComments, boardId = {}", boardId);
		log.info("\t > commentCri = {}", commentCri);
		
		Long userId = obtainUserId(principal);
		log.info("\t > userId = {}", userId);

		List<CommentDetailsDto> pagedComments = commentService.getPagedComments(boardId, commentCri, userId);
		
		Pagination pagination = commentService.getPagination(boardId, commentCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedComments", pagedComments);
		data.put("pagination", pagination);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}
	
	@PrivateApi
	@ApiOperation(value = "댓글 작성", notes = "게시물에 새로운 댓글을 작성한다. (인증 필요)")
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

	@PrivateApi
	@ApiOperation(value = "댓글 수정", notes = "수정할 댓글의 ID(Long commentId)와 댓글 내용이 전달받아 기존의 댓글을 수정한다. (인증 필요)")
	@PatchMapping("/comments/{commentId}") //@PatchMapping(value =  { "/comments/", "/comments/{commentId}" })
	public ResponseEntity<Object> modify(@PathVariable Long commentId, 
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

	@PrivateApi
	@ApiOperation(
		value = "댓글 삭제", 
		notes = "삭제할 댓글의 ID(Long commentId)를 전달받아 기존의 댓글을 삭제한다. (인증 필요)"
	)
	@DeleteMapping("/comments/{commentId}") //@DeleteMapping(value = { "/comments/", "/comments/{commentId}" })
	public ResponseEntity<Object> delete(@PathVariable Long commentId, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## delete, commentId = {}", commentId);
		commentService.deleteComment(commentId, principal.getId()); // update status
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.comment.delete")
				.build());
	}
	
	private Long obtainUserId(PrincipalDetails principal) {
		return (principal == null) ? null : principal.getId();
	}
	
}
