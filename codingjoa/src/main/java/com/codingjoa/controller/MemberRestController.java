package com.codingjoa.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.PasswordDto;
import com.codingjoa.dto.SessionDto;
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
	
	@Resource(name = "emailAuthValidator")
	private Validator emailAuthValidator;
	
	@Resource(name = "passwordValidator")
	private Validator passwordValidator;
	
	@Resource(name = "sessionDto")
	@Lazy
	private SessionDto sessionDto;
	
	@InitBinder("emailAuthDto")
	public void InitBinderEmail(WebDataBinder binder) {
		log.info("## InitBinderEmail");
		log.info("\t > binder target = {}", binder.getTarget());
		log.info("\t > binder target name = {}", binder.getObjectName());
		binder.addValidators(emailAuthValidator);
	}
	
	@InitBinder("passwordDto")
	public void InitBinderPassword(WebDataBinder binder) {
		log.info("## InitBinderPassword");
		log.info("\t > binder target = {}", binder.getTarget());
		log.info("\t > binder target name = {}", binder.getObjectName());
		binder.addValidators(passwordValidator);
	}

	@PostMapping("/email/check")
	public ResponseEntity<Object> checkEmail(@RequestBody @Valid EmailAuthDto emailAuthDto) {
		log.info("## checkEmail");
		log.info("\t > {}", emailAuthDto);

		String authCode = RandomStringUtils.randomNumeric(6);
		log.info("\t > authCode = {}", authCode);
		
		String memberEmail = emailAuthDto.getMemberEmail();
		emailService.sendAuthEmail(memberEmail, authCode);
		redisService.saveAuthCode(memberEmail, authCode);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.CheckEmail"));
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
	
	/*
	 * find-account
	 * find-password
	 * reset-password
	 */
	
	@PostMapping("/password/check")
	public ResponseEntity<Object> checkPassword(@RequestBody @Valid PasswordDto passwordDto, 
			HttpSession session) {
		log.info("## checkPassword");
		log.info("\t > {}", passwordDto);
		
		session.setAttribute("PASSWORD_AUTHENTICATION", true);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.CheckPassword"));
	}
	
	@PutMapping("/password")
	public ResponseEntity<Object> updatePassword(@RequestBody @Valid PasswordDto passwordDto, 
			@AuthenticationPrincipal UserDetailsDto principal, HttpSession session) {
		log.info("## updatePassword");
		log.info("\t > {}", passwordDto);
		
		memberService.updatePassword(passwordDto.getMemberPassword(), principal.getMember().getMemberIdx());
		resetAuthentication(principal.getMember().getMemberId());
		session.setAttribute("PASSWORD_AUTHENTICATION", false);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.UpdatePassword"));
	}
	
	@PostMapping("/findAccount")
	public ResponseEntity<Object> findAccount(@RequestBody @Valid EmailAuthDto emailAuthDto) {
		log.info("## findAccount");
		log.info("\t > {}", emailAuthDto);
		log.info("\t > {}", sessionDto);
		
		String account = memberService.findAccount(emailAuthDto);
		sessionDto.setFindAccountResult(account);
		
		redisService.delete(emailAuthDto.getMemberEmail());
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.FindAccount"));
	}
	
	@PostMapping("/findPassword")
	public ResponseEntity<Object> findPassword(@RequestBody @Valid EmailAuthDto emailAuthDto) {
		log.info("## findPassword");
		log.info("\t > {}", emailAuthDto);
		log.info("\t > {}", sessionDto);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", true);
		resultMap.put("memberId", emailAuthDto.getMemberId());
		sessionDto.setFindPasswordResult(resultMap);
		
		redisService.delete(emailAuthDto.getMemberEmail());
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.FindPassword"));
	}
	
	@PutMapping("/resetPassword")
	public ResponseEntity<Object> resetPassword(@RequestBody @Valid PasswordDto passwordDto) {
		log.info("## resetPassword");
		log.info("\t > {}", passwordDto);
		
		//String memberId = (String) sessionDto.getFindPasswordResult().get("memberId");
		//memberService.updatePassword(passwordDto, memberId);
		
		sessionDto.setFindPasswordResult(null);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.ResetPassword"));
	}
	
	@GetMapping("/test")
	public ResponseEntity<Object> test(HttpSession session) {
		log.info("## test");
		session.removeAttribute("PASSWORD_AUTHENTICATION");
		
		return ResponseEntity.ok(SuccessResponse.create().message("success"));
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
