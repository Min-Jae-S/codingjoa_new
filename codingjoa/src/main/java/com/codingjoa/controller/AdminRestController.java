package com.codingjoa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.annotation.AdminBoardCri;
import com.codingjoa.annotation.AdminUserCri;
import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminUserAuthDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.dto.AdminUserInfoDto;
import com.codingjoa.dto.AdminUserPasswordDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.AdminUserCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.resolver.AdminBoardCriResolver;
import com.codingjoa.resolver.AdminUserCriResolver;
import com.codingjoa.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminRestController {

	private final AdminService adminService;
	private final AdminUserCriResolver adminUserCriResolver;
	private final AdminBoardCriResolver adminBoardCriResolver;
	
	@GetMapping("/users")
	public ResponseEntity<Object> getPagedUsers(@AdminUserCri AdminUserCriteria adminUserCri) {
		log.info("## getPagedUsers");
		
		List<AdminUserDto> pagedUsers = adminService.getPagedUsers(adminUserCri);
		
		log.info("\t > pagedUsers");
		pagedUsers.forEach(adminUserDto -> log.info("\t\t - id: {}, email: {}, roles: {}, provider: {}", 
				adminUserDto.getId(), adminUserDto.getEmail(), adminUserDto.getRoles(), adminUserDto.getProvider()));
		
		Pagination pagination = adminService.getUserPagination(adminUserCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedUsers", pagedUsers);
		data.put("pagination", pagination);
		data.put("adminUserCri", adminUserCri);
		data.put("options", adminUserCriResolver.getOptions());
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}

	@GetMapping("/users/")
	public ResponseEntity<Object> getPagedUsersBySearch(@AdminUserCri AdminUserCriteria adminUserCri) {
		log.info("## getPagedUsers");
		
		List<AdminUserDto> pagedUsers = adminService.getPagedUsers(adminUserCri);
		
		log.info("\t > pagedUsers");
		pagedUsers.forEach(adminUserDto -> log.info("\t\t - id: {}, roles: {}, provider: {}", 
				adminUserDto.getId(), adminUserDto.getRoles(), adminUserDto.getProvider()));
		
		Pagination pagination = adminService.getUserPagination(adminUserCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedUsers", pagedUsers);
		data.put("pagination", pagination);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}
	
	@PutMapping("/users/{userId}/info")
	public ResponseEntity<Object> updateAdminUserInfo(@PathVariable Long userId, @RequestBody AdminUserInfoDto adminUserInfoDto) {
		log.info("## updateAdminUserInfo");
		log.info("\t > userId = {}", userId);
		log.info("\t > adminUserInfoDto = {}", adminUserInfoDto);
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@PutMapping("/users/{userId}/auth")
	public ResponseEntity<Object> updateAdminUserAuth(@PathVariable Long userId, @RequestBody AdminUserAuthDto adminUserAuthDto) {
		log.info("## updateAdminUserAuth");
		log.info("\t > userId = {}", userId);
		log.info("\t > adminUserAuthDto = {}", adminUserAuthDto);
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@PutMapping("/users/{userId}/password")
	public ResponseEntity<Object> updateAdminUserPassword(@PathVariable Long userId, @RequestBody AdminUserPasswordDto adminUserPasswordDto) {
		log.info("## updateAdminUserPassword");
		log.info("\t > userId = {}", userId);
		log.info("\t > adminUserPasswordDto = {}", adminUserPasswordDto);
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@GetMapping("/boards")
	public ResponseEntity<Object> getPagedBoards(@AdminBoardCri AdminBoardCriteria adminBoardCri) {
		log.info("## getPagedBoards");
		log.info("\t > adminBoardCri = {}", adminBoardCri);
		
		List<AdminBoardDto> pagedBoards = adminService.getPagedBoards(adminBoardCri);
		
		log.info("\t > pagedBoard");
		pagedBoards.forEach(adminBoarDto -> 
			log.info("\t\t - id: {}, title: {}", adminBoarDto.getId(), adminBoarDto.getTitle()));
		
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
		
		log.info("\t > pagedBoard");
		pagedBoards.forEach(adminBoarDto -> 
			log.info("\t\t - id: {}, title: {}", adminBoarDto.getId(), adminBoarDto.getTitle()));
		
		Pagination pagination = adminService.getBoardPagination(adminBoardCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedBoards", pagedBoards);
		data.put("pagination", pagination);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}
	
	@DeleteMapping("/users")
	public ResponseEntity<Object> deleteUsers(@RequestBody List<Long> userIds) {
		log.info("## deleteUsers");
		log.info("\t > userIds = {}", userIds);
		
		int deletedRows = adminService.deleteUsers(userIds);
		log.info("\t > deletedRows = {}", deletedRows);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.deleteUsers", deletedRows)
				.build());
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
