package com.codingjoa.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.PasswordDto;
import com.codingjoa.error.SuccessResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.EmailService;
import com.codingjoa.service.MemberService;

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
	
	@Resource(name = "emailValidator")
	private Validator emailValidator;
	
	@Resource(name = "passwordValidator")
	private Validator passwordValidator;

	@InitBinder("emailDto")
	public void InitBinderEmail(WebDataBinder binder) {
		binder.addValidators(emailValidator);
	}
	
	@InitBinder(value = {"passwordDto", "updatePasswordDto"})
	public void InitBinderPassword(WebDataBinder binder) {
		binder.addValidators(passwordValidator);
	}

	@PostMapping("/sendAuthEmail")
	public ResponseEntity<Object> sendAuthEmail(@Valid @RequestBody EmailDto emailDto, 
			BindingResult bindingResult) throws MethodArgumentNotValidException {
		log.info("sendAuthEmail, {}", emailDto);

		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		emailService.sendAuthEmail(emailDto);
		
		return ResponseEntity.ok(SuccessResponse.create().message("success.sendAuthEmail"));
	}
	
	@PutMapping("/updateEmail")
	public ResponseEntity<Object> updateEmail(@Valid @RequestBody EmailDto emailDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal) 
					throws MethodArgumentNotValidException {
		log.info("updateEmail, {}", emailDto);
		
		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		String memberId = principal.getMember().getMemberId();
		memberService.updateEmail(emailDto, memberId);
		
		resetAuthentication(memberId);
		Authentication newAuth = SecurityContextHolder.getContext().getAuthentication();
		
		return ResponseEntity.ok(SuccessResponse.create()
				.message("success.updateEmail").data(newAuth.getPrincipal()));
	}
	
	@PutMapping("/updateAddr")
	public ResponseEntity<Object> updateAddr(@Valid @RequestBody AddrDto addrDto, 
											 @AuthenticationPrincipal UserDetailsDto principal) {
		log.info("updateAddr, {}", addrDto);
		
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
		log.info("updateAgree, {}", agreeDto);
		
		String memberId = principal.getMember().getMemberId();
		memberService.updateAgree(agreeDto, memberId);
		
		resetAuthentication(memberId);
		Authentication newAuth = SecurityContextHolder.getContext().getAuthentication();
		
		return ResponseEntity.ok(SuccessResponse.create()
				.message("success.updateAgree").data(newAuth.getPrincipal()));
	}
	
	@PostMapping("/checkPassword")
	public ResponseEntity<Object> checkPassword(@Valid @RequestBody PasswordDto passwordDto, 
			BindingResult bindingResult, HttpSession session) throws MethodArgumentNotValidException {
		log.info("checkPassword, {}", passwordDto);
		
		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		session.setAttribute("passwordCheck", true);
		
		return ResponseEntity.ok(SuccessResponse.create()
				.message("success.checkPassword").data("/member/updatePassword"));
	}
	
	@PutMapping("/updatePassword")
	public ResponseEntity<Object> updatePassword(@Valid @RequestBody PasswordDto passwordDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal) 
					throws MethodArgumentNotValidException {
		log.info("updatePassword, {}", passwordDto);
		
		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);		
		}
		
		String memberId = principal.getMember().getMemberId();
		memberService.updatePassword(passwordDto, memberId);
		
		resetAuthentication(memberId);
		
		return ResponseEntity.ok(SuccessResponse.create()
				.message("success.updatePassword").data("/member/security"));
	}
	
	@PostMapping("/findAccount")
	public ResponseEntity<Object> findAccount(@Valid @RequestBody EmailDto emailDto, 
			BindingResult bindingResult, HttpSession session) throws MethodArgumentNotValidException {
		log.info("findAccount, {}", emailDto);
		
		if(bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		//session.setAttribute("passwordCheck", true);
		
		return ResponseEntity.ok(SuccessResponse.create()
				.message("success.findAccount").data("/member/showPassword"));
	}
	
	private void resetAuthentication(String memberId) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		Authentication newAuth = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}
	
}
