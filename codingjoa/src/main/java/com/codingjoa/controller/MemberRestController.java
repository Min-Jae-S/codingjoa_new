package com.codingjoa.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
import com.codingjoa.dto.MemberDetailsDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordDto;
import com.codingjoa.entity.Member;
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.EmailService;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.RedisService;
import com.codingjoa.validator.EmailAuthValidator;
import com.codingjoa.validator.EmailValidator;
import com.codingjoa.validator.FindPasswordValidator;
import com.codingjoa.validator.PasswordChangeValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/member")
@RestController
public class MemberRestController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@InitBinder("emailDto")
	public void InitBinderEmail(WebDataBinder binder) {
		binder.addValidators(new EmailValidator());
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
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## sendAuthCodeForUpdate");
		log.info("\t > {}", emailDto);

		String memberEmail = emailDto.getMemberEmail();
		Integer memberIdx = principal.getMember().getMemberIdx();
		memberService.checkEmailForUpdate(memberEmail, memberIdx);
		
		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		emailService.sendAuthCode(memberEmail, authCode);
		redisService.saveKeyAndValue(memberEmail, authCode);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.SendAuthCode").build());
	}
	
	@PutMapping("/email")
	public ResponseEntity<Object> updateEmail(@RequestBody @Valid EmailAuthDto emailAuthDto,
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updateEmail");
		log.info("\t > {}", emailAuthDto);
		
		Member currentMember = principal.getMember();
		memberService.updateEmail(emailAuthDto, currentMember.getMemberIdx());
		redisService.deleteKey(emailAuthDto.getMemberEmail());
		resetAuthentication(currentMember.getMemberId());
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.UpdateEmail").build());
	}
	
	@PutMapping("/address")
	public ResponseEntity<Object> updateAddr(@RequestBody @Valid AddrDto addrDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updateAddr");
		log.info("\t > {}", addrDto);
		
		Member currentMember = principal.getMember();
		memberService.updateAddr(addrDto, currentMember.getMemberIdx());
		resetAuthentication(currentMember.getMemberId());
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.UpdateAddr").build());
	}
	
	@PutMapping("/agree")
	public ResponseEntity<Object> updateAgree(@RequestBody AgreeDto agreeDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updateAgree");
		log.info("\t > {}", agreeDto);
		
		Member currentMember = principal.getMember();
		memberService.updateAgree(agreeDto, currentMember.getMemberIdx());
		resetAuthentication(currentMember.getMemberId());
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.UpdateAgree").build());
	}
	
	@GetMapping("/details")
	public ResponseEntity<Object> getMemberDetails(@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## getMemberDetails");
		// antMatchers("/api/member/details").authenticated() --> principal can't be null
		//MemberDetailsDto memberDetails = (principal != null) ? modelMapper.map(principal, MemberDetailsDto.class) : null;
		MemberDetailsDto memberDetails = modelMapper.map(principal, MemberDetailsDto.class);
		return ResponseEntity.ok(SuccessResponse.builder().data(memberDetails).build());
	}

	@PostMapping("/confirm/password")
	public ResponseEntity<Object> confirmPassword(@RequestBody @Valid PasswordDto passwordDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## confirmPassword");
		log.info("\t > {}", passwordDto);
		
		Member currentMember = principal.getMember();
		memberService.checkCurrentPassword(passwordDto, currentMember.getMemberIdx());
		redisService.saveKeyAndValue(currentMember.getMemberId(), "PASSWORD_CONFIRM");
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.ConfirmPassword").build());
	}
	
	@PutMapping("/password")
	public ResponseEntity<Object> updatePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updatePassword");
		log.info("\t > {}", passwordChangeDto);
		
		Member currentMember = principal.getMember();
		memberService.updatePassword(passwordChangeDto, currentMember.getMemberIdx());
		
		String memberId = currentMember.getMemberId();
		redisService.deleteKey(memberId);
		resetAuthentication(memberId);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.UpdatePassword").build());
	}
	
	@PostMapping("/find/account")
	public ResponseEntity<Object> findAccount(@RequestBody @Valid EmailDto emailDto) {
		log.info("## findAccount");
		log.info("\t > {}", emailDto);
		
		String memberEmail = emailDto.getMemberEmail();
		String memberId = memberService.getMemberIdByEmail(memberEmail);
		log.info("\t > found memberId = {}", memberId);
		
		emailService.sendFoundAccount(memberEmail, memberId);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.FindAccount").build());
	}
	
	@PostMapping("/find/password")
	public ResponseEntity<Object> findPassword(@RequestBody @Valid FindPasswordDto findPasswordDto, 
			HttpServletRequest request) {
		log.info("## findPassword");
		log.info("\t > {}", findPasswordDto);
		
		String memberId = findPasswordDto.getMemberId();
		String memberEmail = findPasswordDto.getMemberEmail();
		Integer memberIdx = memberService.getMemberIdxByIdAndEmail(memberId, memberEmail);
		log.info("\t > found memberIdx = {}", memberIdx);
		
		String key = UUID.randomUUID().toString().replace("-", "");
		log.info("\t > key = {}", key);
		
		String url = ServletUriComponentsBuilder.fromContextPath(request)
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
	
	// TEST
	@DeleteMapping("/test/password-confirm/key")
	public ResponseEntity<Object> removePasswordConfirmKey(@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## removePasswordConfirmKey");
		log.info("\t > principal = {}", principal);
		
		if (principal != null) {
			String memberId = principal.getMember().getMemberId();
			log.info("\t > current hasKey = {}", redisService.hasKey(memberId));
			
			redisService.deleteKey(memberId);
			log.info("\t > after removing key from redis, hasKey = {}", redisService.hasKey(memberId));
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	// TEST
	@DeleteMapping("/test/password-reset/key")
	public ResponseEntity<Object> removePasswordResetKey(@RequestParam String key) {
		log.info("## removePasswordResetKey");

		boolean hasKey = StringUtils.isEmpty(key) ? false : redisService.hasKey(key);
		log.info("\t > current hasKey = {}", hasKey);

		redisService.deleteKey(key);
		log.info("\t > after removing key from redis, hasKey = {}", redisService.hasKey(key));

		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	private void resetAuthentication(String memberId) {
		log.info("## resetAuthentication");
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		Authentication newAuthentication = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	}
}
