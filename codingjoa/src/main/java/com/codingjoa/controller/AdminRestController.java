package com.codingjoa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.BoardInfoDto;
import com.codingjoa.dto.CommentInfoDto;
import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.AdminService;
import com.codingjoa.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminRestController {

	private final AdminService adminService;
	
	@SuppressWarnings("unused")
	private final BoardService boardSerivce;
	
	@GetMapping
	public ResponseEntity<Object> admin() {
		log.info("## adminApi");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test2")
	public ResponseEntity<Object> test2() {
		log.info("## test2");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/test3")
	public ResponseEntity<Object> test3() {
		log.info("## test3");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	@GetMapping("/members")
	public ResponseEntity<Object> getPagedMembers() {
		log.info("## getPagedMembers");
		List<MemberInfoDto> pagedMembers = adminService.getPagedMembers();
		
		Pagination pagination = adminService.getMemberPagination();
		log.info("\t > member pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedMembers", pagedMembers);
		data.put("pagination", pagination);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}

	@GetMapping("/boards")
	public ResponseEntity<Object> getPagedBoards() {
		log.info("## getPagedMembers");
		List<BoardInfoDto> pagedBoards = adminService.getPagedBoards();
		Pagination pagination = adminService.getBoardPagination();
		log.info("\t > board pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedBoards", pagedBoards);
		data.put("pagination", pagination);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}

	@GetMapping("/comments")
	public ResponseEntity<Object> getPagedComments() {
		log.info("## getPagedComments");
		List<CommentInfoDto> pagedComments = adminService.getPagedComments();
		Pagination pagination = adminService.getCommentPagination();
		log.info("\t > comment pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedComments", pagedComments);
		data.put("pagination", pagination);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}
}
