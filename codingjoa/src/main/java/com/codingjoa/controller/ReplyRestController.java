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

import com.codingjoa.annotation.ReplyCri;
import com.codingjoa.dto.ReplyDetailsDto;
import com.codingjoa.dto.ReplyDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.pagination.ReplyCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.ReplyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// https://velog.io/@yoojkim/Rest-API-RESTful%ED%95%98%EA%B2%8C-URL-%EC%84%A4%EA%B3%84%ED%95%98%EA%B8%B0
// REST : 자원(HTTP URI), 행위(HTTP Method), 표현(HTTP Message Payload)

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ReplyRestController {
	
	private final ReplyService replyService;
	
	// https://stackoverflow.com/questions/31680960/spring-initbinder-on-requestbody
	// @InitBinder doesn't work with @RequestBody, it can work with @ModelAttribute Annotation.
	//binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));

	@GetMapping("/boards/{boardId}/replies")
	public ResponseEntity<Object> getPagedReplies(@PathVariable long boardId,
			@ReplyCri ReplyCriteria replyCri, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## getPagedReplies, boardId = {}", boardId);
		log.info("\t > replyCri = {}", replyCri);
		
		Long userId = (principal == null) ? null : principal.getId();
		log.info("\t > userId = {}", userId);

		List<ReplyDetailsDto> pagedReplies = replyService.getPagedReplies(boardId, replyCri, userId);
		
		Pagination pagination = replyService.getPagination(boardId, replyCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedReplies", pagedReplies);
		data.put("pagination", pagination);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}
	
	@PostMapping("/replies")
	public ResponseEntity<Object> writeReply(@Valid @RequestBody ReplyDto replyDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## writeReply");

		replyDto.setUserId(principal.getId());
		replyDto.setStatus(true);
		log.info("\t > replyDto = {}", replyDto);
		
		replyService.saveReply(replyDto);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.WriteReply")
				.build());
	}
	
	@PatchMapping(value = { "/replies/", "/replies/{replyId}" })
	public ResponseEntity<Object> modifyReply(@PathVariable long replyId, 
			@Valid @RequestBody ReplyDto replyDto, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## modifyReply, replyId = {}", replyId);
		
		replyDto.setId(replyId);
		replyDto.setUserId(principal.getId());
		log.info("\t > replyDto = {}", replyDto);

		replyService.updateReply(replyDto);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.UpdateReply")
				.build());
	}
	
	@DeleteMapping(value = { "/replies/", "/replies/{replyId}" })
	public ResponseEntity<Object> deleteReply(@PathVariable long replyId, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## deleteReply, replyId = {}", replyId);
		replyService.deleteReply(replyId, principal.getId()); // update status
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.DeleteReply")
				.build());
	}
	
}
