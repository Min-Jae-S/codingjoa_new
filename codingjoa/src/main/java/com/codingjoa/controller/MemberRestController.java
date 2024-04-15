package com.codingjoa.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.WebDataBinder;
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
		
		Integer memberIdx = principal.getMember().getMemberIdx();
		memberService.updateEmail(emailAuthDto, memberIdx);
		redisService.deleteKey(emailAuthDto.getMemberEmail());
		
		String memberId = principal.getMember().getMemberId();
		resetAuthentication(memberId);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.UpdateEmail").build());
	}
	
	@PutMapping("/address")
	public ResponseEntity<Object> updateAddr(@RequestBody @Valid AddrDto addrDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updateAddr");
		log.info("\t > {}", addrDto);
		
		Integer memberIdx = principal.getMember().getMemberIdx();
		memberService.updateAddr(addrDto, memberIdx);
		
		String memberId = principal.getMember().getMemberId();
		resetAuthentication(memberId);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.UpdateAddr").build());
	}
	
	@PutMapping("/agree")
	public ResponseEntity<Object> updateAgree(@RequestBody AgreeDto agreeDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updateAgree");
		log.info("\t > {}", agreeDto);
		
		Integer memberIdx = principal.getMember().getMemberIdx();
		memberService.updateAgree(agreeDto, memberIdx);
		
		String memberId = principal.getMember().getMemberId();
		resetAuthentication(memberId);
		
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

	@PostMapping("/check/password")
	public ResponseEntity<Object> checkPassword(@RequestBody @Valid PasswordDto passwordDto, 
			@AuthenticationPrincipal UserDetailsDto principal, HttpSession session) {
		log.info("## checkPassword");
		log.info("\t > {}", passwordDto);
		
		Integer memberIdx = principal.getMember().getMemberIdx();
		memberService.checkCurrentPassword(passwordDto, memberIdx);
		session.setAttribute("CHECK_PASSWORD", true);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.CheckPassword").build());
	}
	
	@PutMapping("/password")
	public ResponseEntity<Object> updatePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto, 
			@AuthenticationPrincipal UserDetailsDto principal, HttpSession session) {
		log.info("## updatePassword");
		log.info("\t > {}", passwordChangeDto);
		
		Integer memberIdx = principal.getMember().getMemberIdx();
		memberService.updatePassword(passwordChangeDto, memberIdx);
		
		String memberId = principal.getMember().getMemberId();
		resetAuthentication(memberId);
		session.removeAttribute("CHECK_PASSWORD");
		
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
	public ResponseEntity<Object> resetPassword(@RequestParam(name = "key", required = true) String key,
			@RequestBody @Valid PasswordChangeDto passwordChangeDto) {
		log.info("## resetPassword");
		log.info("\t > key = {}", key);
		log.info("\t > {}", passwordChangeDto);
		
		Integer memberIdx = Integer.parseInt(redisService.findValueByKey(key));
		memberService.updatePassword(passwordChangeDto, memberIdx);
		redisService.deleteKey(key);
		
		return ResponseEntity.ok(SuccessResponse.builder().messageByCode("success.ResetPassword").build());
	}
	
	// test (remove CEHCK_PASSWORD)
	@GetMapping("/test/remove-check-password")
	public ResponseEntity<Object> removeCheckPassword(HttpSession session) {
		log.info("## removeCheckPassword");
		
		Boolean passwordCheck = (Boolean) session.getAttribute("CHECK_PASSWORD");
		log.info("\t > CHECK_PASSWORD = {}", passwordCheck);
		
		if (passwordCheck != null && passwordCheck) {
			session.removeAttribute("CHECK_PASSWORD");
			log.info("\t > after removing CHECK_PASSWORD, CHECK_PASSWORD = {}", session.getAttribute("CHECK_PASSWORD"));
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	// test (remove key)
	@GetMapping("/test/remove-key")
	public ResponseEntity<Object> removeKey(@RequestParam(required = false) String key) {
		log.info("## removeKey");
		
		boolean hasKey = (key == null) ? false : redisService.hasKey(key);
		log.info("\t > hasKey = {}", hasKey);

		if (hasKey) {
			redisService.deleteKey(key);
			log.info("\t > after removing key from redis");
			log.info("\t > hasKey = {}", redisService.hasKey(key));
		}
		
		return ResponseEntity.ok(SuccessResponse.builder().message("success").build());
	}
	
	private void resetAuthentication(String memberId) {
		log.info("## resetAuthentication");
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		Authentication newAuthentication = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(newAuthentication);
		
		//HttpSession session = request.getSession(true);
	    //session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}
}
