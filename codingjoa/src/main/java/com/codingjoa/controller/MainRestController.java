package com.codingjoa.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.PasswordResetDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.enumclass.MailType;
import com.codingjoa.service.EmailService;
import com.codingjoa.service.RedisService;
import com.codingjoa.service.UserService;
import com.codingjoa.util.FormatUtils;
import com.codingjoa.validator.EmailValidator;
import com.codingjoa.validator.PasswordResetValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MainRestController {
	
	private final UserService userService;
	private final EmailService emailService;
	private final RedisService redisService;
	
	@InitBinder("emailDto")
	public void InitBinderEmail(WebDataBinder binder) {
		binder.addValidators(new EmailValidator());
	}

	@InitBinder("passwordResetDto")
	public void InitBinderPasswordReset(WebDataBinder binder) {
		binder.addValidators(new PasswordResetValidator());
	}
	
	@PostMapping("/join/auth-code/send")
	public ResponseEntity<Object> sendAuthCodeForJoin(@RequestBody @Valid EmailDto emailDto) {
		log.info("## sendAuthCodeForJoin");
		log.info("\t > emailDto = {}", emailDto);

		String email = emailDto.getEmail();
		userService.checkEmailForJoin(email);

		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		emailService.send(email, MailType.AUTH_CODE, authCode);
		redisService.saveKeyAndValue(email, authCode);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.join.sendAuthCode")
				.build());
	}
	
	@PostMapping("/password/reset-link/send")
	public ResponseEntity<Object> sendPasswordResetLink(@RequestBody @Valid EmailDto emailDto) {
		log.info("## sendPasswordResetLink");
		log.info("\t > emailDto = {}", emailDto);
		
		String email = emailDto.getEmail();
		Long userId = userService.checkEmailForReset(email);
		String key = UUID.randomUUID().toString().replace("-", "");
		
		redisService.saveKeyAndValue(key, userId);
		
		String passwordResetLink = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/password/reset")
				.queryParam("key", key)
				.build()
				.toString();
		log.info("\t > passwordResetLink = {}", passwordResetLink);
		
		emailService.send(email, MailType.PASSWORD_RESET, passwordResetLink);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.reset-password.sendPasswordResetLink")
				.build());
	}
		
	@PostMapping("/password/reset") // pre-check key parameter in interceptor
	public ResponseEntity<Object> resetPassword(@RequestParam String key, @RequestBody @Valid PasswordResetDto passwordResetDto) {
		log.info("## resetPassword");
		log.info("\t > key = {}", key);
		log.info("\t > passwordResetDto = {}", passwordResetDto);
		
		Long userId = (Long) redisService.findValueByKey(key);
		userService.resetPassword(passwordResetDto, userId);
		redisService.deleteKey(key);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.reset-password.resetPassword")
				.build());
	}
	
	@GetMapping("/saved-request")
	public ResponseEntity<Object> getSavedRequest(HttpServletRequest request, HttpServletResponse response) {
		log.info("## getSavedRequest");
		
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response); // DefaultSavedRequest 
		log.info("\t > savedRequest = {}", savedRequest);
		
		String redirectUrl = (savedRequest == null) ? request.getContextPath() : savedRequest.getRedirectUrl();
		log.info("\t > redirectUrl = {}", FormatUtils.formatString(redirectUrl));
		
		return ResponseEntity.ok(SuccessResponse.builder().data(redirectUrl).build());
	}

}
