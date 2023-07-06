package com.codingjoa.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
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
import com.codingjoa.entity.Member;
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

	@PostMapping("/info/check-email")
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
	
	@PutMapping("info/update-email")
	public ResponseEntity<Object> updateEmail(@RequestBody @Valid EmailAuthDto emailAuthDto,
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updateEmail");
		log.info("\t > {}", emailAuthDto);
		
		Member member = principal.getMember();
		memberService.updateEmail(member.getMemberEmail(), member.getMemberIdx());
		redisService.delete(member.getMemberEmail());
		resetAuthentication(member.getMemberId());
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.UpdateEmail"));
	}
	
	@PutMapping("/updateAddr")
	public ResponseEntity<Object> updateAddr(@RequestBody @Valid AddrDto addrDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updateAddr");
		log.info("\t > {}", addrDto);
		
		String memberId = principal.getMember().getMemberId();
		memberService.updateAddr(addrDto, memberId);
		
		resetAuthentication(memberId);
		Authentication newAuthentication = SecurityContextHolder.getContext().getAuthentication();
		
		return ResponseEntity.ok(SuccessResponse.create()
				.code("success.updateAddr").data(newAuthentication.getPrincipal()));
	}
	
	@PutMapping("/updateAgree")
	public ResponseEntity<Object> updateAgree(@RequestBody AgreeDto agreeDto, 
			@AuthenticationPrincipal UserDetailsDto principal) {
		log.info("## updateAgree");
		log.info("\t > {}", agreeDto);
		
		String memberId = principal.getMember().getMemberId();
		memberService.updateAgree(agreeDto, memberId);
		
		resetAuthentication(memberId);
		Authentication newAuthentication = SecurityContextHolder.getContext().getAuthentication();
		
		return ResponseEntity.ok(SuccessResponse.create()
				.code("success.updateAgree").data(newAuthentication.getPrincipal()));
	}
	
	@PostMapping("/checkPassword")
	public ResponseEntity<Object> checkPassword(@RequestBody @Valid PasswordDto passwordDto, 
			BindingResult bindingResult) throws MethodArgumentNotValidException {
		log.info("## checkPassword");
		log.info("\t > {}", passwordDto);
		log.info("\t > {}", sessionDto);
		
		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		sessionDto.setCheckPasswordResult(true);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.checkPassword"));
	}
	
	@PutMapping("/updatePassword")
	public ResponseEntity<Object> updatePassword(@RequestBody @Valid PasswordDto passwordDto, 
			BindingResult bindingResult, @AuthenticationPrincipal UserDetailsDto principal) 
					throws MethodArgumentNotValidException {
		log.info("## updatePassword");
		log.info("\t > {}", passwordDto);
		
		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);		
		}
		
		String memberId = principal.getMember().getMemberId();
		memberService.updatePassword(passwordDto, memberId);
		
		resetAuthentication(memberId);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.updatePassword"));
	}
	
	@PostMapping("/findAccount")
	public ResponseEntity<Object> findAccount(@RequestBody @Valid EmailAuthDto emailAuthDto, 
			BindingResult bindingResult) throws MethodArgumentNotValidException {
		log.info("## findAccount");
		log.info("\t > {}", emailAuthDto);
		log.info("\t > {}", sessionDto);
		
		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		String account = memberService.findAccount(emailAuthDto);
		sessionDto.setFindAccountResult(account);
		
		redisService.delete(emailAuthDto.getMemberEmail());
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.findAccount"));
	}
	
	@PostMapping("/findPassword")
	public ResponseEntity<Object> findPassword(@RequestBody @Valid EmailAuthDto emailAuthDto, 
			BindingResult bindingResult) throws MethodArgumentNotValidException {
		log.info("## findPassword");
		log.info("\t > {}", emailAuthDto);
		log.info("\t > {}", sessionDto);
		
		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", true);
		resultMap.put("memberId", emailAuthDto.getMemberId());
		sessionDto.setFindPasswordResult(resultMap);
		
		redisService.delete(emailAuthDto.getMemberEmail());
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.findPassword"));
	}
	
	@PutMapping("/resetPassword")
	public ResponseEntity<Object> resetPassword(@RequestBody @Valid PasswordDto passwordDto, 
			BindingResult bindingResult) throws MethodArgumentNotValidException {
		log.info("## resetPassword");
		log.info("\t > {}", passwordDto);
		
		if (bindingResult.hasErrors()) {
			throw new MethodArgumentNotValidException(null, bindingResult);		
		}
		
		String memberId = (String) sessionDto.getFindPasswordResult().get("memberId");
		memberService.updatePassword(passwordDto, memberId);
		
		sessionDto.setFindPasswordResult(null);
		
		return ResponseEntity.ok(SuccessResponse.create().code("success.resetPassword"));
	}
	
	private void resetAuthentication(String memberId) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
		Authentication newAuthentication = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(newAuthentication);
		
		//HttpSession session = request.getSession(true);
	    //session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}
	
}
