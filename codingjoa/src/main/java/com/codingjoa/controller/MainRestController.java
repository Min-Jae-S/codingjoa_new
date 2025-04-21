package com.codingjoa.controller;

import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.PasswordResetDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.enums.MailType;
import com.codingjoa.error.ExpectedException;
import com.codingjoa.security.dto.LoginDto;
import com.codingjoa.service.EmailService;
import com.codingjoa.service.RedisService;
import com.codingjoa.service.UserService;
import com.codingjoa.validator.EmailValidator;
import com.codingjoa.validator.PasswordResetValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Main API")
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
	
	@ApiOperation(value = "회원 가입용 인증코드 전송", notes = "회원 가입시 이메일 인증을 위해 인증코드가 포함된 메일을 발송한다.")
	@PostMapping("/join/auth-code/send")
	public ResponseEntity<Object> sendAuthCodeForJoin(@RequestBody @Valid EmailDto emailDto) {
		log.info("## sendAuthCodeForJoin");
		log.info("\t > emailDto = {}", emailDto);

		String email = emailDto.getEmail();
		userService.checkEmailForJoin(email);

		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		emailService.send(email, MailType.JOIN, authCode);
		redisService.saveKeyAndValue(email, authCode);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.join.sendAuthCode")
				.build());
	}
	
	@ApiOperation(
		value = "비밀번호 재설정 링크 전송",
		notes = "가입 시 등록한 이메일을 입력받아 비밀번호 재설정 링크를 전송한다.\n"
			+ "입력된 이메일이 존재하는 경우, 해당 이메일로 재설정 링크가 포함된 메일을 발송한다."
	)
	@PostMapping("/password/reset-link/send")
	public ResponseEntity<Object> sendPasswordResetLink(@RequestBody @Valid EmailDto emailDto) {
		log.info("## sendPasswordResetLink");
		log.info("\t > emailDto = {}", emailDto);
		
		String email = emailDto.getEmail();
		Long userId = userService.checkEmailForReset(email);
		String token = UUID.randomUUID().toString().replace("-", "");
		
		redisService.saveKeyAndValue(token, userId);
		
		String passwordResetLink = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/password/reset")
				.queryParam("token", token)
				.build()
				.toString();
		log.info("\t > passwordResetLink = {}", passwordResetLink);
		
		emailService.send(email, MailType.PASSWORD_RESET, passwordResetLink);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.reset-password.sendPasswordResetLink")
				.build());
	}
	
	@ApiOperation(value = "비밀번호 재설정", notes = "비밀번호 재설정 링크를 통해 접근한 사용로부터 새로운 비밀번호를 받아, 해당 계정의 비밀번호를 변경한다.")
	@PostMapping("/password/reset")
	public ResponseEntity<Object> resetPassword(@RequestBody @Valid PasswordResetDto passwordResetDto) {
		log.info("## resetPassword");
		log.info("\t > passwordResetDto = {}", passwordResetDto);
		
		String token = passwordResetDto.getToken();
		if (!StringUtils.hasText(token) || !redisService.hasKey(token)) {
			throw new ExpectedException("error.reset-password.notValidToken");
		}
		
		Long userId = (Long) redisService.findValueByKey(token);
		userService.resetPassword(passwordResetDto.getNewPassword(), userId);
		
		redisService.deleteKey(token);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.reset-password.resetPassword")
				.build());
	}
	
	@ApiIgnore
	@GetMapping("/password/reset/token") 
	public ResponseEntity<Object> createPasswordResetToken() {
		log.info("## createPasswordResetToken");
		
		long userId = 1;
		String token = UUID.randomUUID().toString().replace("-", "");
		log.info("\t > created token = {}", token);
		
		redisService.saveKeyAndValue(token, userId);
		
		if (redisService.hasKey(token)) {
			log.info("\t > successfully saved the key");
		} else {
			log.info("\t > failed to save the key");
		}
		
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@ApiIgnore
	@DeleteMapping("/password/reset/token") 
	public ResponseEntity<Object> removePasswordResetToken(@RequestBody Map<String, String> map) {
		log.info("## removePasswordResetToken");
		log.info("\t > map = {}", map);
		
		String token = map.get("token");
		log.info("\t > token = {}", token);
		
		redisService.deleteKey(token);
		
		if (!redisService.hasKey(token)) {
			log.info("\t > successfully removed the token");
		} else {
			log.info("\t > failed to remove the token");
		}
		
		return ResponseEntity.ok(SuccessResponse.create());
	}
	
	@ApiOperation(
		value = "로그인", 
		notes = "로그인 요청을 처리한다.\n"
			+ "실제 인증 처리는 Spring Security의 Filter에서 수행된다.\n"
			+ "이 엔드포인트는 Swagger 문서에서만 확인용으로 제공되며, 응답은 더미이다."
	)
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) {
		log.info("## login");
		return ResponseEntity.ok(SuccessResponse.create());
	}

	@ApiOperation(
		value = "로그아웃", 
		notes = "로그아웃 요청을 처리한다.\n"
			+ "실제 인증 처리는 Spring Security의 Filter에서 수행된다.\n"
			+ "이 엔드포인트는 Swagger 문서에서만 확인용으로 제공되며, 응답은 더미이다."
	)
	@PostMapping("/logout")
	public ResponseEntity<Object> logout() {
		log.info("## logout");
		return ResponseEntity.ok(SuccessResponse.create());
	}

}
