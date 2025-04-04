package com.codingjoa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.annotation.AdminBoardCri;
import com.codingjoa.annotation.AdminUserCri;
import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminUserAuthDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.dto.AdminUserPasswordChangeDto;
import com.codingjoa.dto.AdminUserRegistrationDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.AdminUserCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.resolver.AdminBoardCriResolver;
import com.codingjoa.resolver.AdminUserCriResolver;
import com.codingjoa.service.AdminService;
import com.codingjoa.validator.AdminUserAuthValidator;
import com.codingjoa.validator.AdminUserPasswordChangeValidator;
import com.codingjoa.validator.AdminUserRegistrationValidator;
import com.codingjoa.validator.EmailValidator;
import com.codingjoa.validator.NicknameValidator;

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
	
	@InitBinder("emailDto")
	public void InitBinderEmail(WebDataBinder binder) {
		binder.addValidators(new EmailValidator());
	}
	
	@InitBinder("nicknameDto")
	public void InitBinderNickname(WebDataBinder binder) {
		binder.addValidators(new NicknameValidator());
	}
	
	@InitBinder("adminUserAuthDto")
	public void InitBinderAdminUserAuth(WebDataBinder binder) {
		binder.addValidators(new AdminUserAuthValidator());
	}
	
	@InitBinder("adminUserPasswordChangeDto")
	public void InitBinderAdminUserPasswordChange(WebDataBinder binder) {
		binder.addValidators(new AdminUserPasswordChangeValidator());
	}

	@InitBinder("adminUserRegistrationDto")
	public void InitBinderAdminUserRegistration(WebDataBinder binder) {
		binder.addValidators(new AdminUserRegistrationValidator());
	}
	
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
	
	@PutMapping("/users/{userId}/nickname")
	public ResponseEntity<Object> updateNickname(@PathVariable Long userId, @Valid @RequestBody NicknameDto nicknameDto) {
		log.info("## updateNickname");
		log.info("\t > userId = {}", userId);
		log.info("\t > nicknameDto = {}", nicknameDto);
		
		adminService.updateNickname(nicknameDto, userId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updateNickname")
				.build());
	}
	
	@PutMapping("/users/{userId}/email")
	public ResponseEntity<Object> updateEmail(@PathVariable Long userId, @Valid @RequestBody EmailDto emailDto) {
		log.info("## updateEmail");
		log.info("\t > userId = {}", userId);
		log.info("\t > emailDto = {}", emailDto);
		
		adminService.updateEmail(emailDto, userId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updateEmail")
				.build());
	}
	
	@PutMapping("/users/{userId}/address")
	public ResponseEntity<Object> updateAddress(@PathVariable Long userId, @Valid @RequestBody AddrDto addrDto) {
		log.info("## updateAddress");
		log.info("\t > userId = {}", userId);
		log.info("\t > addrDto = {}", addrDto);
		
		adminService.updateAddr(addrDto, userId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updateAddress")
				.build());
	}
	
	@PutMapping("/users/{userId}/agree")
	public ResponseEntity<Object> updateAgree(@PathVariable Long userId, @RequestBody AgreeDto agreeDto) {
		log.info("## updateAgree");
		log.info("\t > userId = {}", userId);
		log.info("\t > agreeDto = {}", agreeDto);
		
		adminService.updateAgree(agreeDto, userId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updateAgree")
				.build());
	}
	
	@PutMapping("/users/{userId}/password")
	public ResponseEntity<Object> updatePassword(@PathVariable Long userId, @Valid @RequestBody AdminUserPasswordChangeDto adminUserPasswordChangeDto) {
		log.info("## updatePassword");
		log.info("\t > userId = {}", userId);
		log.info("\t > adminUserPasswordChangeDto = {}", adminUserPasswordChangeDto);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updatePassword")
				.build());
	}

	@PutMapping("/users/{userId}/auth")
	public ResponseEntity<Object> updateAuth(@PathVariable Long userId, @Valid @RequestBody AdminUserAuthDto adminUserAuthDto) {
		log.info("## updateAuth");
		log.info("\t > userId = {}", userId);
		log.info("\t > adminUserAuthDto = {}", adminUserAuthDto);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updateAuth")
				.build());
	}
	
	@PostMapping("/users/register")
	public ResponseEntity<Object> registerUser(@Valid @RequestBody AdminUserRegistrationDto adminUserRegistrationDto) {
		log.info("## registerUser");
		log.info("\t > adminUserRegistrationDto = {}", adminUserRegistrationDto);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.registerUser")
				.build());
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<Object> getAdminUser(@PathVariable Long userId) {
		log.info("## getAdminUser");
		log.info("\t > userId = {}", userId);
		
		AdminUserDto adminUser = adminService.getAdminUser(userId);
		log.info("\t > adminUser = {}", adminUser);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(adminUser).build());
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
