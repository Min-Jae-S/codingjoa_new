package com.codingjoa.controller;

import java.time.Duration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.annotation.PrivateApi;
import com.codingjoa.dto.AccountDto;
import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.ImageFileDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordSaveDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.enums.MailType;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.service.EmailService;
import com.codingjoa.service.ImageService;
import com.codingjoa.service.RedisService;
import com.codingjoa.service.UserService;
import com.codingjoa.util.CookieUtils;
import com.codingjoa.validator.EmailAuthValidator;
import com.codingjoa.validator.EmailValidator;
import com.codingjoa.validator.ImageFileValidator;
import com.codingjoa.validator.NicknameValidator;
import com.codingjoa.validator.PasswordChangeValidator;
import com.codingjoa.validator.PasswordSaveValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PrivateApi @Api(tags = "User API")
@Slf4j
@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserRestController {
	
	private static final String JWT_COOKIE = "ACCESS_TOKEN";
	private static final long COOKIE_EXPIRE_SECONDS = Duration.ofHours(1L).getSeconds();
	private final UserService userService;
	private final EmailService emailService;
	private final RedisService redisService;
	private final ImageService imageService;
	private final JwtProvider jwtProvider;
	
	@InitBinder("emailDto")
	public void InitBinderEmail(WebDataBinder binder) {
		binder.addValidators(new EmailValidator());
	}

	@InitBinder("nicknameDto")
	public void InitBinderNickname(WebDataBinder binder) {
		binder.addValidators(new NicknameValidator());
	}
	
	@InitBinder("emailAuthDto")
	public void InitBinderEmailAuth(WebDataBinder binder) {
		binder.addValidators(new EmailAuthValidator(redisService));
	}
	
	@InitBinder("imageFileDto")
	public void initBinderImage(WebDataBinder binder) {
		binder.addValidators(new ImageFileValidator());
	}

	@InitBinder("passwordChangeDto")
	public void InitBinderPasswordChange(WebDataBinder binder) {
		binder.addValidators(new PasswordChangeValidator());
	}

	@InitBinder("passwordSaveDto")
	public void InitBinderPasswordSave(WebDataBinder binder) {
		binder.addValidators(new PasswordSaveValidator());
	}
	
	@ApiOperation(value = "내 정보 조회", notes = "계정 정보를 조회한다. (인증 필요)")
	@GetMapping("/me")
	public ResponseEntity<Object> getAccount(@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## getAccount");
		
		AccountDto account = userService.getAccountById(principal.getId());
		log.info("\t > account = {}", account);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(account).build());
	}
	
	@ApiOperation(value = "이메일 인증 코드 전송", notes = "이메일 변경을 위한 인증 코드가 포함된 메일을 전송한다. (인증 필요)")
	@PostMapping("/me/auth-code/send")
	public ResponseEntity<Object> sendAuthCodeForEmailUpdate(@RequestBody @Valid EmailDto emailDto, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## sendAuthCodeForEmailUpdate");
		log.info("\t > emailDto = {}", emailDto);

		String email = emailDto.getEmail();
		userService.checkEmailForUpdate(email, principal.getId());
		
		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		emailService.send(email, MailType.EMAIL_UPDATE, authCode);
		redisService.saveKeyAndValue(email, authCode);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.sendAuthCode")
				.build());
	}
	
	@ApiOperation(value = "내 정보 수정(닉네임)", notes = "자신의 닉네임을 수정한다. (인증 필요)")
	@PutMapping("/me/nickname")
	public ResponseEntity<Object> updateNickname(@Valid @RequestBody NicknameDto nicknameDto,
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## updateNickname");
		log.info("\t > nicknameDto = {}", nicknameDto);
		
		userService.updateNickname(nicknameDto, principal.getId());
		
		PrincipalDetails newPrincipal = userService.getUserDetailsById(principal.getId());
		refreshJwt(newPrincipal, request, response);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.updateNickname")
				.build());
	}
	
	@ApiOperation(value = "내 정보 수정(이메일)", notes = "자신의 이메일(인증된 이메일)을 수정한다. (인증 필요)")
	@PutMapping("/me/email")
	public ResponseEntity<Object> updateEmail(@Valid @RequestBody EmailAuthDto emailAuthDto,
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## updateEmail");
		log.info("\t > emailAuthDto = {}", emailAuthDto);
		
		userService.updateEmail(emailAuthDto, principal.getId());
		redisService.deleteKey(emailAuthDto.getEmail());
		
		PrincipalDetails newPrincipal = userService.getUserDetailsById(principal.getId());
		refreshJwt(newPrincipal, request, response);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.updateEmail")
				.build());
	}
	
	@ApiOperation(value = "내 정보 수정(주소)", notes = "자신의 주소(우편번호, 기본주소, 상세주소)를 수정한다. (인증 필요)")
	@PutMapping("/me/address")
	public ResponseEntity<Object> updateAddress(@Valid @RequestBody AddrDto addrDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## updateAddress");
		log.info("\t > addrDto = {}", addrDto);
		
		userService.updateAddr(addrDto, principal.getId());
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.updateAddress")
				.build());
	}
	
	@ApiOperation(value = "내 정보 수정(수신 동의)", notes = "수신 동의를 수정한다. (인증 필요)")
	@PutMapping("/me/agree")
	public ResponseEntity<Object> updateAgree(@RequestBody AgreeDto agreeDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## updateAgree");
		log.info("\t > agreeDto = {}", agreeDto);
		
		userService.updateAgree(agreeDto, principal.getId());
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.updateAgree")
				.build());
	}
	
	@ApiOperation(value = "프로필 이미지 업로드", notes = "프로필 이미지를 업로드하여 새로운 이미지를 등록하거나 기존 이미지를 수정한다. (인증 필요)")
	@PostMapping("/me/image")
	public ResponseEntity<Object> saveImageWithUpload(@Valid @ModelAttribute ImageFileDto imageFileDto,
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## saveImageWithUpload");
		
		imageService.saveUserImageWithUpload(imageFileDto.getFile(), principal.getId());
		
		PrincipalDetails newPrincipal = userService.getUserDetailsById(principal.getId());
		refreshJwt(newPrincipal, request, response);
		
		return ResponseEntity.ok().body(SuccessResponse.builder()
				.messageByCode("success.user.saveImageWithUpload")
				.build());
	}
	
	@ApiOperation(value = "내 정보 수정(비밀번호)", notes = "기존 비밀번호를 수정한다. (인증 필요)")
	@PutMapping("/me/password")
	public ResponseEntity<Object> updatePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto, 
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## updatePassword");
		log.info("\t > passwordChangeDto = {}", passwordChangeDto);
		
		userService.updatePassword(passwordChangeDto, principal.getId());
		
		PrincipalDetails newPrincipal = userService.getUserDetailsById(principal.getId());
		refreshJwt(newPrincipal, request, response);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.updatePassword")
				.build());
	}
	
	@ApiOperation(
		value = "비밀번호 등록", 
		notes = "SNS 계정을 통해 가입한 사용자는 초기 비밀번호가 설정되지 않으며, 이에 해당하는 사용자에 대해 비밀번호를 새로 등록한다. (인증 필요)"
	)
	@PostMapping("/me/password")
	public ResponseEntity<Object> savePassword(@Valid @RequestBody PasswordSaveDto passwordDto, 
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## savePassword");
		log.info("\t > passwordDto = {}", passwordDto);
		
		userService.savePassword(passwordDto, principal.getId());
		
		PrincipalDetails newPrincipal = userService.getUserDetailsById(principal.getId());
		refreshJwt(newPrincipal, request, response);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.savePassword")
				.build());
	}
	
	private void refreshJwt(PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## refreshJwt");
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
		String jwt = jwtProvider.createJwt(authentication, request);
		CookieUtils.addCookie(request, response, JWT_COOKIE, jwt, COOKIE_EXPIRE_SECONDS);
	}
	
}
