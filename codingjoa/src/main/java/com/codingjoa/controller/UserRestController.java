package com.codingjoa.controller;

import java.time.Duration;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.FindPasswordDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordSaveDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.dto.ImageFileDto;
import com.codingjoa.dto.UserInfoDto;
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
import com.codingjoa.validator.NicknameValidator;
import com.codingjoa.validator.PasswordChangeValidator;
import com.codingjoa.validator.PasswordSaveValidator;
import com.codingjoa.validator.ImageFileValidator;

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

	@InitBinder("findPasswordDto")
	public void InitBinderFindPassword(WebDataBinder binder) {
		binder.addValidators(new FindPasswordValidator());
	}
	
	@InitBinder("passwordChangeDto")
	public void InitBinderPasswordChange(WebDataBinder binder) {
		binder.addValidators(new PasswordChangeValidator());
	}

	@InitBinder("passwordSaveDto")
	public void InitBinderPasswordSave(WebDataBinder binder) {
		binder.addValidators(new PasswordSaveValidator());
	}
	
	@InitBinder("imageFileDto")
	public void initBinderUpload(WebDataBinder binder) {
		binder.addValidators(new ImageFileValidator());
	}

	@PostMapping("/join/auth")
	public ResponseEntity<Object> sendAuthCodeForJoin(@RequestBody @Valid EmailDto emailDto) {
		log.info("## sendAuthCodeForJoin");
		log.info("\t > emailDto = {}", emailDto);

		String email = emailDto.getEmail();
		userService.checkEmailForJoin(email);

		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		emailService.sendAuthCode(email, authCode);
		redisService.saveKeyAndValue(email, authCode);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.join.SendAuthCode")
				.build());
	}
	
	@GetMapping("/account")
	public ResponseEntity<Object> getUserInfo(@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## getUserInfo");
		
		UserInfoDto userInfo = userService.getUserInfoById(principal.getId());
		log.info("\t > userInfo = {}", userInfo);
		
		return ResponseEntity.ok(SuccessResponse.builder().data(userInfo).build());
	}
	
	@PostMapping("/account/email/auth")
	public ResponseEntity<Object> sendAuthCodeForEmailUpdate(@RequestBody @Valid EmailDto emailDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## sendAuthCodeForEmailUpdate");
		log.info("\t > emailDto = {}", emailDto);

		String email = emailDto.getEmail();
		userService.checkEmailForUpdate(email, principal.getId());
		
		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		emailService.sendAuthCode(email, authCode);
		redisService.saveKeyAndValue(email, authCode);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.SendAuthCode")
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
				.messageByCode("success.user.UpdateNickname")
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
				.messageByCode("success.user.UpdateEmail")
				.build());
	}
	
	@PutMapping("/account/address")
	public ResponseEntity<Object> updateAddress(@RequestBody @Valid AddrDto addrDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## updateAddress");
		log.info("\t > addrDto = {}", addrDto);
		
		userService.updateAddr(addrDto, principal.getId());
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.UpdateAddress")
				.build());
	}
	
	@PutMapping("/account/agree")
	public ResponseEntity<Object> updateAgree(@RequestBody AgreeDto agreeDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## updateAgree");
		log.info("\t > agreeDto = {}", agreeDto);
		
		userService.updateAgree(agreeDto, principal.getId());
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.user.UpdateAgree")
				.build());
	}
	
	@PostMapping("/account/image")
	public ResponseEntity<Object> updateImageWithUpload(@ModelAttribute @Valid ImageFileDto imageFileDto,
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## updateImageWithUpload");
		
		imageService.updateUserImageWithUpload(imageFileDto.getFile(), principal.getId());
		
		PrincipalDetails newPrincipal = userService.getUserDetailsById(principal.getId());
		addJwtCookie(newPrincipal, request, response);
		
		return ResponseEntity.ok().body(SuccessResponse.builder()
				.messageByCode("success.user.UpdateImageWithUpload")
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
				.messageByCode("success.user.UpdatePassword")
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
				.messageByCode("success.user.SavePassword")
				.build());
	}
	
	// findPassword --> sendResetPasswordUrl
	@PostMapping("/find/password")
	public ResponseEntity<Object> findPassword(@RequestBody @Valid FindPasswordDto findPasswordDto) {
		log.info("## findPassword");
		log.info("\t > findPasswordDto = {}", findPasswordDto);
		
		//String memberId = findPasswordDto.getMemberId();
		//String memberEmail = findPasswordDto.getMemberEmail();
		//Long memberIdx = userService.getMemberIdxByIdAndEmail(memberId, memberEmail);
		//log.info("\t > found memberIdx = {}", memberIdx);
		
		String key = UUID.randomUUID().toString().replace("-", "");
		log.info("\t > key = {}", key);
		
		String resetPasswordUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/user/resetPassword")
				.queryParam("key", key)
				.build()
				.toString();
		log.info("\t > resetPasswordUrl = {}", resetPasswordUrl);
		
		String email = findPasswordDto.getMemberEmail();
		emailService.sendResetPasswordUrl(email, resetPasswordUrl);
		redisService.saveKeyAndValue(key, email);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.FindPassword")
				.build());
	}
	
	@PostMapping("/password")
	public ResponseEntity<Object> resetPassword(@RequestParam String key, @RequestBody @Valid PasswordChangeDto passwordChangeDto) {
		log.info("## resetPassword");
		log.info("\t > key = {}", key);
		log.info("\t > passwordChangeDto = {}", passwordChangeDto);
		
		Integer userId = Integer.parseInt(redisService.findValueByKey(key));
		userService.updatePassword(passwordChangeDto, userId);
		redisService.deleteKey(key);
		
		return ResponseEntity.ok(SuccessResponse.builder()
				.messageByCode("success.ResetPassword")
				.build());
	}
	
	private void addJwtCookie(PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
		String jwt = jwtProvider.createJwt(authentication, request);
		CookieUtils.addCookie(request, response, JWT_COOKIE, jwt, COOKIE_EXPIRE_SECONDS);
	}
	
}
