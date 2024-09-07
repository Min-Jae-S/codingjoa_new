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
import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordSaveDto;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.dto.UploadFileDto;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.service.EmailService;
import com.codingjoa.service.ImageService;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.CookieUtils;
import com.codingjoa.validator.EmailAuthValidator;
import com.codingjoa.validator.EmailValidator;
import com.codingjoa.validator.FindPasswordValidator;
import com.codingjoa.validator.NicknameValidator;
import com.codingjoa.validator.PasswordChangeValidator;
import com.codingjoa.validator.PasswordSaveValidator;
import com.codingjoa.validator.UploadFileValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
public class MemberRestController {
	
	private static final String JWT_COOKIE = "ACCESS_TOKEN";
	private static final long COOKIE_EXPIRE_SECONDS = Duration.ofHours(1l).getSeconds();
	private final MemberService memberService;
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
	
	@InitBinder("uploadFileDto")
	public void initBinderUpload(WebDataBinder binder) {
		binder.addValidators(new UploadFileValidator());
	}

	@PostMapping("/join/auth")
	public ResponseEntity<Object> sendAuthCodeForJoin(@RequestBody @Valid EmailDto emailDto) {
		log.info("## sendAuthCodeForJoin");
		log.info("\t > {}", emailDto);

		String memberEmail = emailDto.getMemberEmail();
		memberService.checkEmailForJoin(memberEmail);

		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		emailService.sendAuthCode(memberEmail, authCode);
		redisService.saveKeyAndValue(memberEmail, authCode);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.SendAuthCode").build());
	}
	
	@PostMapping("/update-email/auth")
	public ResponseEntity<Object> sendAuthCodeForUpdate(@RequestBody @Valid EmailDto emailDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## sendAuthCodeForUpdate");
		log.info("\t > {}", emailDto);

		String memberEmail = emailDto.getMemberEmail();
		memberService.checkEmailForUpdate(memberEmail, principal.getIdx());
		
		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		emailService.sendAuthCode(memberEmail, authCode);
		redisService.saveKeyAndValue(memberEmail, authCode);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.SendAuthCode").build());
	}
	
	@PostMapping("/account/image")
	public ResponseEntity<Object> updateImage(@ModelAttribute @Valid UploadFileDto uploadFileDto,
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## updateImage");
		imageService.uploadMemberImage(uploadFileDto.getFile(), principal.getIdx());
		
		PrincipalDetails newPrincipal = memberService.getUserDetailsByIdx(principal.getIdx());
		addJwtCookie(newPrincipal, request, response);
		
		return ResponseEntity.ok().body(SuccessResponse.builder().messageByCode("success.UpdateMemberImage").build());
	}
	
	@PutMapping("/account/nickname")
	public ResponseEntity<Object> updateNickname(@RequestBody @Valid NicknameDto nicknameDto,
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## updateNickname");
		log.info("\t > {}", nicknameDto);
		memberService.updateNickname(nicknameDto, principal.getIdx());
		
		PrincipalDetails newPrincipal = memberService.getUserDetailsByIdx(principal.getIdx());
		addJwtCookie(newPrincipal, request, response);
		
		return ResponseEntity.ok().body(SuccessResponse.builder().messageByCode("success.UpdateNickname").build());
	}
	
	@PutMapping("/account/email")
	public ResponseEntity<Object> updateEmail(@RequestBody @Valid EmailAuthDto emailAuthDto,
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## updateEmail");
		log.info("\t > {}", emailAuthDto);
		memberService.updateEmail(emailAuthDto, principal.getIdx());
		redisService.deleteKey(emailAuthDto.getMemberEmail());
		
		PrincipalDetails newPrincipal = memberService.getUserDetailsByIdx(principal.getIdx());
		addJwtCookie(newPrincipal, request, response);
		
		return ResponseEntity.ok().body(SuccessResponse.builder().messageByCode("success.UpdateEmail").build());
	}
	
	@PutMapping("/account/address")
	public ResponseEntity<Object> updateAddress(@RequestBody @Valid AddrDto addrDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## updateAddress");
		log.info("\t > {}", addrDto);
		memberService.updateAddr(addrDto, principal.getIdx());
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.UpdateAddress").build());
	}
	
	@PutMapping("/account/agree")
	public ResponseEntity<Object> updateAgree(@RequestBody AgreeDto agreeDto, 
			@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## updateAgree");
		log.info("\t > {}", agreeDto);
		memberService.updateAgree(agreeDto, principal.getIdx());
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.UpdateAgree").build());
	}
	
	@PutMapping("/account/password")
	public ResponseEntity<Object> updatePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto, 
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## updatePassword");
		log.info("\t > {}", passwordChangeDto);
		memberService.updatePassword(passwordChangeDto, principal.getIdx());
		
		PrincipalDetails newPrincipal = memberService.getUserDetailsByIdx(principal.getIdx());
		addJwtCookie(newPrincipal, request, response);
		
		return ResponseEntity.ok().body(SuccessResponse.builder().messageByCode("success.UpdatePassword").build());
	}
	
	@PostMapping("/account/password")
	public ResponseEntity<Object> savePassword(@RequestBody @Valid PasswordSaveDto passwordDto, 
			@AuthenticationPrincipal PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		log.info("## savePassword");
		log.info("\t > {}", passwordDto);
		memberService.savePassword(passwordDto, principal.getIdx());
		
		PrincipalDetails newPrincipal = memberService.getUserDetailsByIdx(principal.getIdx());
		addJwtCookie(newPrincipal, request, response);
		
		return ResponseEntity.ok().body(SuccessResponse.builder().messageByCode("success.SavePassword").build());
	}
	
	@GetMapping("/account")
	public ResponseEntity<Object> getMemberInfo(@AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## getMemberInfo");
		MemberInfoDto memberInfo = memberService.getMemberInfoByIdx(principal.getIdx());
		
		//return ResponseEntity.ok(SuccessResponse.builder().data(memberInfo).build());
		return ResponseEntity.ok(SuccessResponse.builder().details(memberInfo).build());
	}
	
//	@PostMapping("/find/account")
//	public ResponseEntity<Object> findAccount(@RequestBody @Valid EmailDto emailDto) {
//		log.info("## findAccount");
//		log.info("\t > {}", emailDto);
//		
//		String memberEmail = emailDto.getMemberEmail();
//		String memberId = memberService.getMemberIdByEmail(memberEmail);
//		log.info("\t > found memberId = {}", memberId);
//		
//		emailService.sendFoundAccount(memberEmail, memberId);
//		
//		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.FindAccount").build());
//	}
	
	@PostMapping("/find/password")
	public ResponseEntity<Object> findPassword(@RequestBody @Valid FindPasswordDto findPasswordDto) {
		log.info("## findPassword");
		log.info("\t > {}", findPasswordDto);
		
		String memberId = findPasswordDto.getMemberId();
		String memberEmail = findPasswordDto.getMemberEmail();
		Integer memberIdx = memberService.getMemberIdxByIdAndEmail(memberId, memberEmail);
		log.info("\t > found memberIdx = {}", memberIdx);
		
		String key = UUID.randomUUID().toString().replace("-", "");
		log.info("\t > key = {}", key);
		
		String url = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/member/resetPassword")
				.queryParam("key", key)
				.build()
				.toString();
		log.info("\t > attached URL = {}", url);
		
		emailService.sendResetPasswordUrl(memberEmail, memberId, url);
		redisService.saveKeyAndValue(key, memberIdx.toString());
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.FindPassword").build());
	}
	
	@PutMapping("/reset/password")
	public ResponseEntity<Object> resetPassword(@RequestParam /* (required = true) */ String key, // pre-check in interceptor 
			@RequestBody @Valid PasswordChangeDto passwordChangeDto) {
		log.info("## resetPassword");
		log.info("\t > key = {}", key);
		log.info("\t > {}", passwordChangeDto);
		
		Integer memberIdx = Integer.parseInt(redisService.findValueByKey(key));
		memberService.updatePassword(passwordChangeDto, memberIdx);
		redisService.deleteKey(key);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.ResetPassword").build());
	}
	
	private void addJwtCookie(PrincipalDetails principal, HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
		String jwt = jwtProvider.createJwt(authentication, request);
		CookieUtils.addCookie(request, response, JWT_COOKIE, jwt, COOKIE_EXPIRE_SECONDS);
	}
	
}
