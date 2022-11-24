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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
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
import com.codingjoa.error.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.EmailService;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.RedisService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/member")
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
		binder.addValidators(emailAuthValidator);
	}
	
	@InitBinder("passwordDto")
	public void InitBinderPassword(WebDataBinder binder) {
		binder.addValidators(passwordValidator);
	}

	@PostMapping("/sendAuthEmail")
	public ResponseEntity<Object> sendAuthEmail(@Valid @RequestBody EmailAuthDto emailAuthDto, 
			BindingResult bindingResult) throws MethodArgumentNotValidException {
		log.info("{}", emailAuthDto);

		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		String authCode = RandomStringUtils.randomAlphanumeric(6);
		log.info("authCode = {}", authCode);
		
		String memberEmail = emailAuthDto.getMemberEmail();
		emailService.sendAuthEmail(memberEmail, authCode);
		redisService.saveAuthCode(memberEmail, authCode);
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.sendAuthEmail"));
	}
	
	@PutMapping("/updateEmail")
	public ResponseEntity<Object> updateEmail(@Valid @RequestBody EmailAuthDto emailAuthDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal) 
					throws MethodArgumentNotValidException {
		log.info("{}", emailAuthDto);
		
		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		String memberId = principal.getMember().getMemberId();
		memberService.updateEmail(emailAuthDto, memberId);

		redisService.delete(emailAuthDto.getMemberEmail());
		
		resetAuthentication(memberId);
		Authentication newAuth = SecurityContextHolder.getContext().getAuthentication();
		
		return ResponseEntity.ok(SuccessResponse.create()
				.message("success.updateEmail").data(newAuth.getPrincipal()));
	}
	
	@PutMapping("/updateAddr")
	public ResponseEntity<Object> updateAddr(@Valid @RequestBody AddrDto addrDto, 
											 @AuthenticationPrincipal UserDetailsDto principal) {
		log.info("{}", addrDto);
		
		String memberId = principal.getMember().getMemberId();
		memberService.updateAddr(addrDto, memberId);
		
		resetAuthentication(memberId);
		Authentication newAuth = SecurityContextHolder.getContext().getAuthentication();
		
		return ResponseEntity.ok(SuccessResponse.create()
				.message("success.updateAddr").data(newAuth.getPrincipal()));
	}
	
	@PutMapping("/updateAgree")
	public ResponseEntity<Object> updateAgree(@RequestBody AgreeDto agreeDto, 
											  @AuthenticationPrincipal UserDetailsDto principal) {
		log.info("{}", agreeDto);
		
		String memberId = principal.getMember().getMemberId();
		memberService.updateAgree(agreeDto, memberId);
		
		resetAuthentication(memberId);
		Authentication newAuth = SecurityContextHolder.getContext().getAuthentication();
		
		return ResponseEntity.ok(SuccessResponse.create()
				.message("success.updateAgree").data(newAuth.getPrincipal()));
	}
	
	@PostMapping("/checkPassword")
	public ResponseEntity<Object> checkPassword(@Valid @RequestBody PasswordDto passwordDto, 
			BindingResult bindingResult) throws MethodArgumentNotValidException {
		log.info("{}", passwordDto);
		log.info("{}", sessionDto);
		
		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		sessionDto.setCheckPasswordResult(true);
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.checkPassword"));
	}
	
	@PutMapping("/updatePassword")
	public ResponseEntity<Object> updatePassword(@Valid @RequestBody PasswordDto passwordDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal) 
					throws MethodArgumentNotValidException {
		log.info("{}", passwordDto);
		
		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);		
		}
		
		String memberId = principal.getMember().getMemberId();
		memberService.updatePassword(passwordDto, memberId);
		
		resetAuthentication(memberId);
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.updatePassword"));
	}
	
	@PostMapping("/findAccount")
	public ResponseEntity<Object> findAccount(@Valid @RequestBody EmailAuthDto emailAuthDto, 
			BindingResult bindingResult) throws MethodArgumentNotValidException {
		log.info("{}", emailAuthDto);
		log.info("{}", sessionDto);
		
		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		String account = memberService.findAccount(emailAuthDto);
		sessionDto.setFindAccountResult(account);
		
		redisService.delete(emailAuthDto.getMemberEmail());
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.findAccount"));
	}
	
	@PostMapping("/findPassword")
	public ResponseEntity<Object> findPassword(@Valid @RequestBody EmailAuthDto emailAuthDto, 
			BindingResult bindingResult, HttpSession session) throws MethodArgumentNotValidException {
		log.info("{}", emailAuthDto);
		log.info("{}", sessionDto);
		
		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", true);
		resultMap.put("memberId", emailAuthDto.getMemberId());
		resultMap.put("memberEmail", emailAuthDto.getMemberEmail());
		sessionDto.setFindPasswordResult(resultMap);
		
		redisService.delete(emailAuthDto.getMemberEmail());
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.findPassword"));
	}
	
	@PutMapping("/resetPassword")
	public ResponseEntity<Object> resetPassword(@Valid @RequestBody PasswordDto passwordDto, 
			BindingResult bindingResult) throws MethodArgumentNotValidException {
		log.info("{}", passwordDto);
		
		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);		
		}
		
		Map<String, Object> resultMap = sessionDto.getFindPasswordResult();
		String memberId = (String) resultMap.get("memberId");
		String memberEmail = (String) resultMap.get("memberEmail");
		
		//memberService.resetPassword(passwordDto, memberId, memberEmail);
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.resetPassword"));
	}
	
	private void resetAuthentication(String memberId) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		Authentication newAuth = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}
	
}
