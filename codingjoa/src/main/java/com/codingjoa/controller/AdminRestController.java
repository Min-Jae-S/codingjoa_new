package com.codingjoa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.annotation.AdminBoardCri;
import com.codingjoa.annotation.AdminCommentCri;
import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminCommentDto;
import com.codingjoa.dto.AccountDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.AdminCommentCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.resolver.AdminBoardCriResolver;
import com.codingjoa.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminRestController {

	private final AdminService adminService;
	private final AdminBoardCriResolver adminBoardCriResolver;
	
	@GetMapping
	public ResponseEntity<Object> admin() {
		log.info("## adminApi");
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}

	@GetMapping("/members")
	public ResponseEntity<Object> getPagedMembers() {
		log.info("## getPagedMembers");
		
		List<AccountDto> pagedMembers = adminService.getPagedMembers();
		Pagination pagination = adminService.getMemberPagination();
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedMembers", pagedMembers);
		data.put("pagination", pagination);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}

	@GetMapping("/boards")
	public ResponseEntity<Object> getPagedBoards(@AdminBoardCri AdminBoardCriteria adminBoardCri) {
		log.info("## getPagedBoards");
		log.info("\t > adminBoardCri = {}", adminBoardCri);
		
		List<AdminBoardDto> pagedBoards = adminService.getPagedBoards(adminBoardCri);
		
		Pagination pagination = adminService.getBoardPagination(adminBoardCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedBoards", pagedBoards);
		data.put("pagination", pagination);
		data.put("adminBoardCri", adminBoardCri);
		data.put("options", adminBoardCriResolver.getOptions());
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}

	@GetMapping("/boards/")
	public ResponseEntity<Object> getPagedBoardsBySearch(@AdminBoardCri AdminBoardCriteria adminBoardCri) {
		log.info("## getPagedBoardsBySearch");
		log.info("\t > adminBoardCri = {}", adminBoardCri);
		
		List<AdminBoardDto> pagedBoards = adminService.getPagedBoards(adminBoardCri);
		
		Pagination pagination = adminService.getBoardPagination(adminBoardCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedBoards", pagedBoards);
		data.put("pagination", pagination);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}
	
	@GetMapping("/comments")
	public ResponseEntity<Object> getPagedComments() {
		log.info("## getPagedComments");
		
		AdminCommentCriteria adminCommentCri = AdminCommentCriteria.create();
		log.info("\t > create default adminCommentCri = {}", adminCommentCri);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(null).build());
	}
	
	@GetMapping("/comments/")
	public ResponseEntity<Object> getPagedComments(@AdminCommentCri AdminCommentCriteria adminCommentCri) {
		log.info("## getPagedComments");
		log.info("\t > adminCommentCri = {}", adminCommentCri);
		
		List<AdminCommentDto> pagedComments = adminService.getPagedComments();
		
		Pagination pagination = adminService.getCommentPagination();
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedComments", pagedComments);
		data.put("pagination", pagination);
		data.put("adminCommentCri", adminCommentCri);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}
	
	@DeleteMapping("/boards")
	public ResponseEntity<Object> deleteBoards(@RequestBody List<Integer> boardIds) {
		log.info("## deleteBoards");
		log.info("\t > boardIds = {}", boardIds);
		
		int deletedRows = adminService.deleteBoards(boardIds);
		log.info("\t > deletedRows = {}", deletedRows);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.DeleteBoards", deletedRows)
				.build());
	}

	@DeleteMapping("/comments")
	public ResponseEntity<Object> deleteComments(@RequestBody List<Integer> commentIds) {
		log.info("## deleteComments");
		log.info("\t > commentIds = {}", commentIds);
		
		int deletedRows = adminService.deleteComments(commentIds);
		log.info("\t > deletedRows = {}", deletedRows);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.DeleteComments", deletedRows)
				.build());
	}
}
