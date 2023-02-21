package com.codingjoa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.ReplyDetailsDto;
import com.codingjoa.error.SuccessResponse;
import com.codingjoa.service.ReplyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/reply")
@RestController
public class ReplyRestController {
	
	@Autowired
	private ReplyService replyService;

	@GetMapping("/{boardIdx}/replies")
	public ResponseEntity<Object> main(@PathVariable int boardIdx) {
		log.info("boardIdx={}", boardIdx);
		
		List<ReplyDetailsDto> replyList = replyService.getPagedReply();
		
		return ResponseEntity.ok(SuccessResponse.create().data(replyList));
	}
	
	@PostMapping("")
	public ResponseEntity<Object> writeProc() {
		
		return null;
	}
}
