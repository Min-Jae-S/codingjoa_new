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
import com.codingjoa.enumclass.MailType;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.service.EmailService;
import com.codingjoa.service.ImageService;
import com.codingjoa.service.RedisService;
import com.codingjoa.service.UserService;
import com.codingjoa.util.CookieUtils;
import com.codingjoa.validator.EmailAuthValidator;
import com.codingjoa.validator.EmailValidator;
import com.codingjoa.validator.FindPasswordValidator;
import com.codingjoa.validator.ImageFileValidator;
import com.codingjoa.validator.NicknameValidator;
import com.codingjoa.validator.PasswordChangeValidator;
import com.codingjoa.validator.PasswordSaveValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/user")
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
	public void InitBinderNicknmae(WebDataBinder binder) {
		binder.addValidators(new NicknameValidator());
	}
	
	@InitBinder("emailAuthDto")
	public void InitBinderEmailAuth(WebDataBinder binder) {
		binder.addValidators(new EmailAuthValidator(redisService));
	}
	
	@InitBinder("imageFileDto")
	public void initBinderUpload(WebDataBinder binder) {
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
	
	@InitBinder("findPasswordDto")
	public void InitBinderFindPassword(WebDataBinder binder) {
		binder.addValidators(new FindPasswordValidator());
	}
	
	@GetMapping("/account")
	public ResponseEntity<Object> getAccount(@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## getAccount");
		
		AccountDto account = userService.getAccountById(principal.getId());
		log.info("\t > account = {}", account);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(account).build());
	}
	
	@PostMapping("/account/email/auth-code/send")
	public ResponseEntity<Object> sendAuthCodeForEmailUpdate(@RequestBody @Valid EmailDto emailDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## sendAuthCodeForEmailUpdate");
		log.info("\t > emailDto = {}", emailDto);

		String email = emailDto.getEmail();
		userService.checkEmailForUpdate(email, principal.getId());
		
		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		emailService.send(email, MailType.AUTH_CODE, authCode);
		redisService.saveKeyAndValue(email, authCode);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.sendAuthCode")
				.build());
	}
	
	@PutMapping("/account/nickname")
	public ResponseEntity<Object> updateNickname(@RequestBody @Valid NicknameDto nicknameDto,
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## updateNickname");
		log.info("\t > nicknameDto = {}", nicknameDto);
		
		userService.updateNickname(nicknameDto, principal.getId());
		
		PrincipalDetails newPrincipal = userService.getUserDetailsById(principal.getId());
		addJwtCookie(newPrincipal, request, response);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.updateNickname")
				.build());
	}
	
	@PutMapping("/account/email")
	public ResponseEntity<Object> updateEmail(@RequestBody @Valid EmailAuthDto emailAuthDto,
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## updateEmail");
		log.info("\t > emailAuthDto = {}", emailAuthDto);
		
		userService.updateEmail(emailAuthDto, principal.getId());
		redisService.deleteKey(emailAuthDto.getEmail());
		
		PrincipalDetails newPrincipal = userService.getUserDetailsById(principal.getId());
		addJwtCookie(newPrincipal, request, response);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.updateEmail")
				.build());
	}
	
	@PutMapping("/account/address")
	public ResponseEntity<Object> updateAddress(@RequestBody @Valid AddrDto addrDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## updateAddress");
		log.info("\t > addrDto = {}", addrDto);
		
		userService.updateAddr(addrDto, principal.getId());
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.updateAddress")
				.build());
	}
	
	@PutMapping("/account/agree")
	public ResponseEntity<Object> updateAgree(@RequestBody AgreeDto agreeDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## updateAgree");
		log.info("\t > agreeDto = {}", agreeDto);
		
		userService.updateAgree(agreeDto, principal.getId());
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.updateAgree")
				.build());
	}
	
	@PostMapping("/account/image")
	public ResponseEntity<Object> saveImageWithUpload(@ModelAttribute @Valid ImageFileDto imageFileDto,
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## saveImageWithUpload");
		
		imageService.saveUserImageWithUpload(imageFileDto.getFile(), principal.getId());
		
		PrincipalDetails newPrincipal = userService.getUserDetailsById(principal.getId());
		addJwtCookie(newPrincipal, request, response);
		
		return ResponseEntity.ok().body(SuccessResponse.builder()
				.messageByCode("success.user.saveImageWithUpload")
				.build());
	}
	
	@PutMapping("/account/password")
	public ResponseEntity<Object> updatePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto, 
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## updatePassword");
		log.info("\t > passwordChangeDto = {}", passwordChangeDto);
		
		userService.updatePassword(passwordChangeDto, principal.getId());
		
		PrincipalDetails newPrincipal = userService.getUserDetailsById(principal.getId());
		addJwtCookie(newPrincipal, request, response);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.updatePassword")
				.build());
	}
	
	@PostMapping("/account/password")
	public ResponseEntity<Object> savePassword(@RequestBody @Valid PasswordSaveDto passwordDto, 
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## savePassword");
		log.info("\t > passwordDto = {}", passwordDto);
		
		userService.savePassword(passwordDto, principal.getId());
		
		PrincipalDetails newPrincipal = userService.getUserDetailsById(principal.getId());
		addJwtCookie(newPrincipal, request, response);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.savePassword")
				.build());
	}
	
	private void addJwtCookie(PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
		String jwt = jwtProvider.createJwt(authentication, request);
		CookieUtils.addCookie(request, response, JWT_COOKIE, jwt, COOKIE_EXPIRE_SECONDS);
	}
	
}
