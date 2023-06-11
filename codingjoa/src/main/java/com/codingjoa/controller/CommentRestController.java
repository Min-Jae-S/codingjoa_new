package com.codingjoa.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentDto;
import com.codingjoa.pagination.CommentCriteria;
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.CommentService;

import lombok.extern.slf4j.Slf4j;

// https://velog.io/@yoojkim/Rest-API-RESTful%ED%95%98%EA%B2%8C-URL-%EC%84%A4%EA%B3%84%ED%95%98%EA%B8%B0
// 자원(HTTP URI), 행위(HTTP Method), 표현(HTTP Message Payload)

@Slf4j
@RequestMapping("/api")
@RestController
public class CommentRestController {
	
	@Autowired
	private CommentService commentService;
	
	@Resource(name = "commentValidator")
	private Validator commentValidator;
	
	@InitBinder(value = "commentDto")
	public void initBinderComment(WebDataBinder binder) {
		log.info("## initBinderComment");
		log.info("\t > binder target = {}", binder.getTarget());
		log.info("\t > binder target name = {}", binder.getObjectName());
		binder.addValidators(commentValidator);
		
		// https://stackoverflow.com/questions/31680960/spring-initbinder-on-requestbody
		// @InitBinder doesn't work with @RequesBody, it can work with @ModelAttribute Annotation.
		//binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}

	@GetMapping("/boards/{boardIdx}/comments")
	public ResponseEntity<Object> getCommentList(@PathVariable Integer boardIdx, @CommentCri CommentCriteria commentCri) {
		log.info("boardIdx = {}", boardIdx);
		log.info("commentCri = {}", commentCri);
		
		List<CommentDetailsDto> commentList = commentService.getPagedComment(boardIdx, commentCri);
		
		return ResponseEntity.ok(SuccessResponse.create().data(commentList));
	}
	
	@GetMapping(value = { "/comments", "/comments/{commentIdx}" })
	public ResponseEntity<Object> getComment(@PathVariable("commentIdx") Integer commentIdx, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("commentIdx = {}", commentIdx);
		log.info("memberId = {}", principal.getMember().getMemberId());
		
		int commentWriterIdx = principal.getMember().getMemberIdx();
		CommentDetailsDto commentDetails = commentService.getCommentDetails(commentIdx, commentWriterIdx);
		
		return ResponseEntity.ok(SuccessResponse.create().data(commentDetails));
	}
	
	@PostMapping("/comments")
	public ResponseEntity<Object> writeComment(@Valid @RequestBody CommentDto commentDto,
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal) 
					throws MethodArgumentNotValidException {
		log.info("commentDto = {}", commentDto);
		log.info("memberId = {}", principal.getMember().getMemberId());
		
		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		int commentWriterIdx = principal.getMember().getMemberIdx();
		commentDto.setCommentWriterIdx(commentWriterIdx);
		commentDto.setCommentUse(true);
		commentService.writeComment(commentDto);
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.writeComment"));
	}
	
	@PatchMapping(value = { "/comments", "/comments/{commentIdx}" })
	public ResponseEntity<Object> modifyComment(@PathVariable Integer commentIdx) {
		log.info("commentIdx = {}", commentIdx);
		// update ...
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.updateComment"));
	}
	
	
	@DeleteMapping(value = { "/comments", "/comments/{commentIdx}" })
	public ResponseEntity<Object> deleteComment(@PathVariable Integer commentIdx) {
		log.info("commentIdx = {}", commentIdx);
		commentService.deleteComment(commentIdx);
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.deleteComment"));
	}
	
}
