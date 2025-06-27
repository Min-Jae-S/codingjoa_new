package com.codingjoa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
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

import com.codingjoa.annotation.AdminBoardCri;
import com.codingjoa.annotation.AdminUserCri;
import com.codingjoa.annotation.PrivateApi;
import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminUserAddrDto;
import com.codingjoa.dto.AdminUserAuthDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.dto.AdminUserPasswordChangeDto;
import com.codingjoa.dto.AdminUserRegistrationDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.BoardCacheCountDto;
import com.codingjoa.dto.CommentCacheCountDto;
import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.AdminUserCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.resolver.AdminBoardCriResolver;
import com.codingjoa.resolver.AdminUserCriResolver;
import com.codingjoa.service.AdminService;
import com.codingjoa.service.RedisService;
import com.codingjoa.service.UserService;
import com.codingjoa.util.RedisKeyUtils;
import com.codingjoa.validator.AdminUserAddrValidator;
import com.codingjoa.validator.AdminUserAuthValidator;
import com.codingjoa.validator.AdminUserPasswordChangeValidator;
import com.codingjoa.validator.AdminUserRegistrationValidator;
import com.codingjoa.validator.EmailValidator;
import com.codingjoa.validator.NicknameValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PrivateApi @Api(tags = "Admin API", description = "AdminRestController")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminRestController {

	private final AdminService adminService;
	private final UserService userService;
	private final AdminUserCriResolver adminUserCriResolver;
	private final AdminBoardCriResolver adminBoardCriResolver;
	private final RedisService redisService;
	
	@InitBinder("emailDto")
	public void InitBinderAdminUserEmail(WebDataBinder binder) {
		binder.addValidators(new EmailValidator());
	}
	
	@InitBinder("nicknameDto")
	public void InitBinderAdminUserNickname(WebDataBinder binder) {
		binder.addValidators(new NicknameValidator());
	}
	
	@InitBinder("adminUserAddrDto")
	public void InitBinderAdminUserAddr(WebDataBinder binder) {
		binder.addValidators(new AdminUserAddrValidator());
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
		binder.addValidators(new AdminUserRegistrationValidator(userService));
	}
	
	@ApiOperation(value = "회원 목록 조회", notes = "회원 목록과 pagination, 뷰를 구성하기 위한 요소를 조회한다. (관리자 권한 필요)")
	@GetMapping("/users")
	public ResponseEntity<Object> getPagedUsers(@AdminUserCri AdminUserCriteria adminUserCri) {
		log.info("## getPagedUsers");
		
		List<AdminUserDto> pagedUsers = adminService.getPagedUsers(adminUserCri);
		
		Pagination pagination = adminService.getUserPagination(adminUserCri);
		log.info("\t > pagination = {}", pagination);
		
		Map<String, Object> data = new HashMap<>();
		data.put("pagedUsers", pagedUsers);
		data.put("pagination", pagination);
		data.put("adminUserCri", adminUserCri);
		data.put("options", adminUserCriResolver.getOptions());
		
		return ResponseEntity.ok(SuccessResponse.builder().data(data).build());
	}

	@ApiOperation(value = "회원 목록 검색 조회", notes = "검색 조건을 기준으로 회원 목록과 pagination을 조회한다. (관리자 권한 필요)")
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
	
	@ApiOperation(value = "회원 닉네임 수정", notes = "회원 ID(Long userId)와 닉네임 정보를 전달받아 닉네임을 수정한다. (관리자 권한 필요)")
	@PatchMapping("/users/{userId}/nickname")
	public ResponseEntity<Object> updateNickname(@PathVariable Long userId, @Valid @RequestBody NicknameDto nicknameDto) {
		log.info("## updateNickname");
		log.info("\t > userId = {}", userId);
		log.info("\t > nicknameDto = {}", nicknameDto);
		
		adminService.updateNickname(nicknameDto, userId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updateNickname")
				.build());
	}
	
	@ApiOperation(value = "회원 이메일 수정", notes = "회원 ID(Long userId)와 이메일 정보를 전달받아 이메일을 수정한다. (관리자 권한 필요)")
	@PatchMapping("/users/{userId}/email")
	public ResponseEntity<Object> updateEmail(@PathVariable Long userId, @Valid @RequestBody EmailDto emailDto) {
		log.info("## updateEmail");
		log.info("\t > userId = {}", userId);
		log.info("\t > emailDto = {}", emailDto);
		
		adminService.updateEmail(emailDto, userId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updateEmail")
				.build());
	}
	
	@ApiOperation(value = "회원 주소 수정", notes = "회원 ID(Long userId)와 주소 정보(우편번호, 기본주소, 상세주소)를 전달받아 주소를 수정한다. (관리자 권한 필요)")
	@PatchMapping("/users/{userId}/address")
	public ResponseEntity<Object> updateAddress(@PathVariable Long userId, @Valid @RequestBody AdminUserAddrDto adminUserAddrDto) {
		log.info("## updateAddress");
		log.info("\t > userId = {}", userId);
		log.info("\t > adminUserAddrDto = {}", adminUserAddrDto);
		
		adminService.updateAddr(adminUserAddrDto, userId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updateAddress")
				.build());
	}
	
	@ApiOperation(value = "회원 동의 정보 수정", notes = "회원 ID(Long userId)와 동의 정보를 동의 여부를 수정한다. (관리자 권한 필요)")
	@PatchMapping("/users/{userId}/agree")
	public ResponseEntity<Object> updateAgree(@PathVariable Long userId, @RequestBody AgreeDto agreeDto) {
		log.info("## updateAgree");
		log.info("\t > userId = {}", userId);
		log.info("\t > agreeDto = {}", agreeDto);
		
		adminService.updateAgree(agreeDto, userId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updateAgree")
				.build());
	}
	
	@ApiOperation(value = "회원 비밀번호 수정", notes = "회원 ID(Long userId)와 비밀번호 정보를 전달받아 기존 비밀번호를 수정한다. (관리자 권한 필요)")
	@PatchMapping("/users/{userId}/password")
	public ResponseEntity<Object> updatePassword(@PathVariable Long userId, @Valid @RequestBody AdminUserPasswordChangeDto adminUserPasswordChangeDto) {
		log.info("## updatePassword");
		log.info("\t > userId = {}", userId);
		log.info("\t > adminUserPasswordChangeDto = {}", adminUserPasswordChangeDto);

		adminService.updatePassword(adminUserPasswordChangeDto, userId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updatePassword")
				.build());
	}

	@ApiOperation(value = "회원 권한 수정", notes = "회원 ID(Long userId)와 권한 정보를 전달받아 회원의 권한을 부여하거나 삭제한다. (관리자 권한 필요)")
	@PatchMapping("/users/{userId}/auth")
	public ResponseEntity<Object> updateAuth(@PathVariable Long userId, @Valid @RequestBody AdminUserAuthDto adminUserAuthDto) {
		log.info("## updateAuth");
		log.info("\t > userId = {}", userId);
		log.info("\t > adminUserAuthDto = {}", adminUserAuthDto);
		
		adminService.updateAuth(adminUserAuthDto, userId);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.updateAuth")
				.build());
	}
	
	@ApiOperation(value = "회원 등록", notes = "신규 회원 정보를 전달받아 회원을 등록한다. (관리자 권한 필요)")
	@PostMapping("/users/register")
	public ResponseEntity<Object> registerUser(@Valid @RequestBody AdminUserRegistrationDto adminUserRegistrationDto) {
		log.info("## registerUser");
		log.info("\t > adminUserRegistrationDto = {}", adminUserRegistrationDto);
		
		adminService.registerUser(adminUserRegistrationDto);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.admin.registerUser")
				.build());
	}
	
	@ApiOperation(value = "회원 상세정보 조회", notes = "회원 ID(Long userId)를 전달받아 회원의 상세정보(기본정보, 권한, SNS정보)를 조회한다. (관리자 권한 필요)")
	@GetMapping("/users/{userId}")
	public ResponseEntity<Object> getUser(@PathVariable Long userId) {
		log.info("## getUser");
		log.info("\t > userId = {}", userId);
		
		AdminUserDto user = adminService.getUser(userId);
		log.info("\t > user = {}", user);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(user).build());
	}

	@ApiOperation(value = "게시글 목록 조회", notes = "게시글 목록과 pagination, 뷰를 구성하기 위한 요소를 조회한다.")
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

	@ApiOperation(value = "게시글 목록 검색 조회", notes = "검색 조건을 기준으로 게시글 목록과 pagination을 조회한다. (관리자 권한 필요)")
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
	
	@ApiOperation(value = "회원 삭제", notes = "삭제할 회원의 ID 리스트를 전달받아 해당 회원들을 삭제한다. (관리자 권한 필요)")
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
	
	@ApiOperation(value = "게시글 삭제", notes = "삭제할 게시글의 ID 리스트를 전달받아 해당 게시글들을 삭제한다. (관리자 권한 필요)")
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
	
	@GetMapping("/redis/count/board")
	public ResponseEntity<Object> getCacheCountInBoard() {
		log.info("## getCacheCountInBoard");
		Map<Long, BoardCacheCountDto> map = new HashMap<>();
		
		Set<String> keys = redisService.keys("board:*:*");
		for (String key : keys) {
			String[] parts = key.split(":");
			
			Long boardId = Long.parseLong(parts[1]);
			BoardCacheCountDto dto = map.computeIfAbsent(boardId, k -> {
				BoardCacheCountDto newDto = new BoardCacheCountDto();
				newDto.setId(boardId);
				return newDto;
			});
			
			String countType = parts[2];
			int countDelta = redisService.getDelta(key);
			
			if ("view_count".equals(countType)) {
				dto.setViewCount(countDelta);
			} else if ("comment_count".equals(countType)) {
				dto.setCommentCount(countDelta);
			} else if ("like_count".equals(countType)) {
				dto.setLikeCount(countDelta);
			}
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().data(map.values()).build());
	}
	
	@GetMapping("/redis/count/comment")
	public ResponseEntity<Object> getCacheCountInComment() {
		log.info("## getCacheCountInComment");
		List<CommentCacheCountDto> commentCacheCount = new ArrayList<>();
		
		Set<String> keys = redisService.keys("comment:*:like_count");
		for (String key : keys) {
			Long commentId = RedisKeyUtils.extractEntityId(key);
			int countDelta = redisService.getDelta(key);
			commentCacheCount.add(CommentCacheCountDto.of(commentId, countDelta));
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().data(commentCacheCount).build());
	}
	

}
