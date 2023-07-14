package com.codingjoa.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordDto;
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.EmailService;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.RedisService;

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
	
	@Resource(name = "emailValidator")
	private Validator emailValidator;

	@Resource(name = "emailAuthValidator")
	private Validator emailAuthValidator;
	
	@Resource(name = "passwordChangeValidator")
	private Validator passwordChangeValidator;
	
	@InitBinder("emailDto")
	public void InitBinderEmail(WebDataBinder binder) {
		binder.addValidators(emailValidator);
	}

	@InitBinder("emailAuthDto")
	public void InitBinderEmailAuth(WebDataBinder binder) {
		binder.addValidators(emailAuthValidator);
	}
	
	@InitBinder("passwordChangeDto")
	public void InitBinderPasswordChange(WebDataBinder binder) {
		binder.addValidators(passwordChangeValidator);
	}

	@PostMapping("/join/auth")
	public ResponseEntity<Object> sendAuthCodeForJoin(@RequestBody @Valid EmailDto emailDto) {
		log.info("## sendAuthCodeForJoin");
		log.info("\t > {}", emailDto);

		String memberEmail = emailDto.getMemberEmail();
		memberService.checkEmailForJoin(memberEmail);

		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		emailService.sendAuthEmail(memberEmail, authCode);
		redisService.saveAuthCode(memberEmail, authCode);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.SendAuthCode"));
	}
	
	@PostMapping("/update-email/auth")
	public ResponseEntity<Object> sendAuthCodeForUpdate(@RequestBody @Valid EmailDto emailDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## sendAuthCodeForUpdate");
		log.info("\t > {}", emailDto);

		String memberEmail = emailDto.getMemberEmail();
		memberService.checkEmailForUpdate(memberEmail, principal.getMember().getMemberIdx());
		
		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		emailService.sendAuthEmail(memberEmail, authCode);
		redisService.saveAuthCode(memberEmail, authCode);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.SendAuthCode"));
	}
	
	@PostMapping("/reset-password/auth")
	public ResponseEntity<Object> sendEmailForReset(@RequestBody @Valid EmailDto emailDto) {
		log.info("## sendEmailForReset");
		log.info("\t > {}", emailDto);
		
//		String memberEmail = emailDto.getMemberEmail();
//
//		String authCode = RandomStringUtils.randomNumeric(6);
//		log.info("\t > authCode = {}", authCode);
//		
//		emailService.sendAuthEmail(memberEmail, authCode);
//		redisService.saveAuthCode(memberEmail, authCode);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.SendAuthCode"));
	}
	
	@PutMapping("/email")
	public ResponseEntity<Object> updateEmail(@RequestBody @Valid EmailAuthDto emailAuthDto,
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updateEmail");
		log.info("\t > {}", emailAuthDto);
		
		String memberEmail = emailAuthDto.getMemberEmail();
		memberService.updateEmail(memberEmail, principal.getMember().getMemberIdx());
		redisService.delete(memberEmail);
		resetAuthentication(principal.getMember().getMemberId());
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.UpdateEmail"));
	}
	
	@PutMapping("/address")
	public ResponseEntity<Object> updateAddr(@RequestBody @Valid AddrDto addrDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updateAddr");
		log.info("\t > {}", addrDto);
		
		memberService.updateAddr(addrDto.getMemberZipcode(), addrDto.getMemberAddr(), 
				addrDto.getMemberAddrDetail(), principal.getMember().getMemberIdx());
		resetAuthentication(principal.getMember().getMemberId());
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.UpdateAddr"));
	}
	
	@PutMapping("/agree")
	public ResponseEntity<Object> updateAgree(@RequestBody AgreeDto agreeDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updateAgree");
		log.info("\t > {}", agreeDto);
		
		memberService.updateAgree(agreeDto.isMemberAgree(), principal.getMember().getMemberIdx());
		resetAuthentication(principal.getMember().getMemberId());

		return ResponseEntity.ok(SuccessResponse.create().code("success.UpdateAgree"));
	}
	
	@GetMapping("/current-member")
	public ResponseEntity<Object> getCurrentMember(@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## getCurrentMember");
		return ResponseEntity.ok(SuccessResponse.create().data(principal.getMember()));
	}
	
	@PostMapping("/check/password")
	public ResponseEntity<Object> checkPassword(@RequestBody @Valid PasswordDto passwordDto, 
			@AuthenticationPrincipal UserDetailsDto principal, HttpSession session) {
		log.info("## checkPassword");
		log.info("\t > {}", passwordDto);
		
		memberService.checkCurrentPassword(passwordDto.getMemberPassword(), principal.getMember().getMemberIdx());
		session.setAttribute("PASSWORD_AUTHENTICATION", true);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.CheckPassword"));
	}
	
	@PutMapping("/password")
	public ResponseEntity<Object> updatePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto, 
			@AuthenticationPrincipal UserDetailsDto principal, HttpSession session) {
		log.info("## updatePassword");
		log.info("\t > {}", passwordChangeDto);
		
		memberService.updatePassword(passwordChangeDto.getMemberPassword(), principal.getMember().getMemberIdx());
		resetAuthentication(principal.getMember().getMemberId());
		session.removeAttribute("PASSWORD_AUTHENTICATION");
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.UpdatePassword"));
	}
	
	@PostMapping("/send/found-account")
	public ResponseEntity<Object> sendFoundAccount(@RequestBody @Valid EmailAuthDto emailAuthDto) {
		log.info("## sendFoundAccount");
		log.info("\t > {}", emailAuthDto);
		
		String memberEmail = emailAuthDto.getMemberEmail();
		String memberId = memberService.findIdByEmail(memberEmail);
		emailService.sendFoundAccountEmail(memberEmail, memberId);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.SendFoundAccount"));
	}

	@PostMapping("/check/account")
	public ResponseEntity<Object> checkAccount(@RequestBody @Valid EmailAuthDto emailAuthDto, 
			HttpSession session) {
		log.info("## checkAccount");
		log.info("\t > {}", emailAuthDto);
		
		redisService.delete(emailAuthDto.getMemberEmail());
		session.setAttribute("ACCOUNT_AUTHENTICATION", emailAuthDto.getMemberId());
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.FindPassword"));
	}
	
	@PutMapping("/reset/password")
	public ResponseEntity<Object> resetPassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto, 
			@SessionAttribute("ACCOUNT_AUTHENTICATION") String memberId) {
		log.info("## resetPassword");
		log.info("\t > {}", passwordChangeDto);
		log.info("\t > session memberId = {}", memberId);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.ResetPassword"));
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
