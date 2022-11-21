package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.EmailDto;
import com.codingjoa.enumclass.Type;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.RedisService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "emailValidator")
public class EmailValidator implements Validator {

	private final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

	@Autowired
	private MemberService memberService;

	@Autowired
	private RedisService redisService;

	@Override
	public boolean supports(Class<?> clazz) {
		return EmailDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== EmailValidator ==============");

		EmailDto emailDto = (EmailDto) target;
		Type type = emailDto.getType();
		
		if (type == null) {
			errors.rejectValue("memberEmail", "NotValidAccess");
			return;
		}
		
		String memberEmail = emailDto.getMemberEmail();
		String authCode = emailDto.getAuthCode();
		
		if (!StringUtils.hasText(memberEmail)) {
			errors.rejectValue("memberEmail", "NotBlank");
			return;
		} 
		
		if (!Pattern.matches(EMAIL_REGEXP, memberEmail)) {
			errors.rejectValue("memberEmail", "Pattern");
			return;
		}

		if (type == Type.JOIN) {
			if (memberService.isEmailExist(memberEmail)) {
				errors.rejectValue("memberEmail", "EmailExist");
			}
			return;
		} 
		
		if (type == Type.BEFORE_UPDATE) {
			if (memberService.isMyEmail(memberEmail, getCurrentId())) {
				errors.rejectValue("memberEmail", "NotMyEmail");
				return;
			}
			
			if (memberService.isEmailExist(memberEmail)) {
				errors.rejectValue("memberEmail", "EmailExist");
				return;
			}
			return;
		}
		
		if (type == Type.UPDATE) {
			if (memberService.isMyEmail(memberEmail, getCurrentId())) {
				errors.rejectValue("memberEmail", "NotMyEmail");
				return;
			}
			
			if (memberService.isEmailExist(memberEmail)) {
				errors.rejectValue("memberEmail", "EmailExist");
				return;
			}
			
			if (!redisService.hasAuthCode(memberEmail)) {
				errors.reject("memberEmail", "NotAuthCodeExist");
				return;
			}
			
			if (!StringUtils.hasText(authCode)) {
				errors.rejectValue("authCode", "NotBlank");
				return;
			} 
			
			if (!redisService.isAuthCodeValid(memberEmail, authCode)) {
				errors.rejectValue("authCode", "NotValid");
				return;
			}
			return;
		} 
		
		if (type == Type.BEFORE_FIND_ACCOUNT) {
			if (!memberService.isEmailExist(memberEmail)) {
				errors.rejectValue("memberEmail", "NotEmailExist");
			}
			return;
		} 
		
		if (type == Type.FIND_ACCOUNT) {
			if (!memberService.isEmailExist(memberEmail)) {
				errors.rejectValue("memberEmail", "NotEmailExist");
			}
			
			if (!redisService.hasAuthCode(memberEmail)) {
				errors.reject("memberEmail", "NotAuthCodeExist");
				return;
			}
			
			if (!StringUtils.hasText(authCode)) {
				errors.rejectValue("authCode", "NotBlank");
				return;
			} 
			
			if (!redisService.isAuthCodeValid(memberEmail, authCode)) {
				errors.rejectValue("authCode", "NotValid");
				return;
			}
			return;
		}
	}
	
	private String getCurrentId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null) return null;

		Object principal = auth.getPrincipal();
		String currentId = null;

		if (principal instanceof UserDetailsDto) {
			UserDetailsDto userDetailsDto = (UserDetailsDto) principal;
			currentId = userDetailsDto.getMember().getMemberId();
		} else if (principal instanceof String) {
			currentId = null; // principal = anonymousUser
		}

		return currentId;
	}

}
