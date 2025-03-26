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
import com.codingjoa.annotation.AdminUserCri;
import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.AdminUserCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.resolver.AdminBoardCriResolver;
import com.codingjoa.resolver.AdminUserCriResolver;
import com.codingjoa.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminRestController {

	private final AdminService adminService;
	private final AdminBoardCriResolver adminBoardCriResolver;
	private final AdminUserCriResolver adminUserCriResolver;
	
	@GetMapping("/users")
	public ResponseEntity<Object> getPagedUsers(AdminUserCriteria adminUserCri) {
		log.info("## getPagedUsers");
		
		List<AdminUserDto> pagedUsers = adminService.getPagedUsers(adminUserCri);
		Pagination pagination = adminService.getUserPagination(adminUserCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedUsers", pagedUsers);
		data.put("pagination", pagination);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}

	@GetMapping("/users/")
	public ResponseEntity<Object> getPagedUsersBySearch(@AdminUserCri AdminUserCriteria adminUserCri) {
		log.info("## getPagedUsers");
		
		List<AdminUserDto> pagedUsers = adminService.getPagedUsers(adminUserCri);
		Pagination pagination = adminService.getUserPagination(adminUserCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedUsers", pagedUsers);
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
	
	@DeleteMapping("/boards")
	public ResponseEntity<Object> deleteBoards(@RequestBody List<Long> boardIds) {
		log.info("## deleteBoards");
		log.info("\t > boardIds = {}", boardIds);
		
		int deletedRows = adminService.deleteBoards(boardIds);
		log.info("\t > deletedRows = {}", deletedRows);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.deleteBoards", deletedRows)
				.build());
	}

}
