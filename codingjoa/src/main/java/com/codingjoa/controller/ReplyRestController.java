package com.codingjoa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.ReplyDto;
import com.codingjoa.error.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/reply")
@RestController
public class ReplyRestController {

	@GetMapping("/{boardIdx}/replies")
	public ResponseEntity<Object> readReplies(@PathVariable int boardIdx) {
		log.info("boardIdx={}", boardIdx);
		
		return ResponseEntity.ok(SuccessResponse.create()
				.data(new ReplyDto(boardIdx)));
	}
}
