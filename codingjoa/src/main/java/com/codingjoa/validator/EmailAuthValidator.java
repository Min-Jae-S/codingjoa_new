package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.enumclass.Type;
import com.codingjoa.service.RedisService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "emailAuthValidator")
public class EmailAuthValidator implements Validator {

	private final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

	@Autowired
	private RedisService redisService;

	@Override
	public boolean supports(Class<?> clazz) {
		return EmailAuthDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		EmailAuthDto emailAuthDto = (EmailAuthDto) target;
		Type type = emailAuthDto.getType();
		log.info("\t > type = {}", type);
		
		String memberId = emailAuthDto.getMemberId();
		String memberEmail = emailAuthDto.getMemberEmail();
		String authCode = emailAuthDto.getAuthCode();
		
		// UPDATE_EMAIL	 		(info.jsp)
		// FIND_PASSWORD		(find-password.jsp)
		
		// BEFORE_JOIN			(join.jsp)
		// BEFORE_UPDATE_EMAIL	(info.jsp)
		// FIND_ACCOUNT			(find-account.jsp)
		// BEOFRE_FIND_PASSWORD (find-password.jsp)
		
		if (type == Type.FIND_PASSWORD) { 
			if (!StringUtils.hasText(memberId)) {
				errors.rejectValue("memberId", "NotBlank");
				return;
			}
			
			if (!StringUtils.hasText(memberEmail)) {
				errors.rejectValue("memberEmail", "NotBlank");
				return;
			} 
			
			if (!Pattern.matches(EMAIL_REGEXP, memberEmail)) {
				errors.rejectValue("memberEmail", "Pattern");
				return;
			}
			
			if (!redisService.hasAuthCode(memberEmail)) {
				errors.rejectValue("memberEmail", "NotAuthCodeExist");
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
		}
		
//		if (type == Type.UPDATE_EMAIL) {
//			if (!StringUtils.hasText(memberEmail)) {
//				errors.rejectValue("memberEmail", "NotBlank");
//				return;
//			} 
//			
//			if (!Pattern.matches(EMAIL_REGEXP, memberEmail)) {
//				errors.rejectValue("memberEmail", "Pattern");
//				return;
//			}
//			
//			if (!redisService.hasAuthCode(memberEmail)) {
//				errors.rejectValue("memberEmail", "NotAuthCodeExist");
//				return;
//			}
//			
//			if (!StringUtils.hasText(authCode)) {
//				errors.rejectValue("authCode", "NotBlank");
//				return;
//			} 
//			
//			if (!redisService.isAuthCodeValid(memberEmail, authCode)) {
//				errors.rejectValue("authCode", "NotValid");
//				return;
//			}
//		} 
		
//		if (type == Type.BEFORE_JOIN) {
//			if (!StringUtils.hasText(memberEmail)) {
//				errors.rejectValue("memberEmail", "NotBlank");
//				return;
//			} 
//			
//			if (!Pattern.matches(EMAIL_REGEXP, memberEmail)) {
//				errors.rejectValue("memberEmail", "Pattern");
//				return;
//			}
//			
//			if (memberService.isEmailExist(memberEmail)) {
//				errors.rejectValue("memberEmail", "EmailExist");
//				return;
//			}
//			
//			return;
//		} 
		
//		if (type == Type.BEFORE_UPDATE_EMAIL) {
//			if (!StringUtils.hasText(memberEmail)) {
//				errors.rejectValue("memberEmail", "NotBlank");
//				return;
//			} 
//			
//			if (!Pattern.matches(EMAIL_REGEXP, memberEmail)) {
//				errors.rejectValue("memberEmail", "Pattern");
//				return;
//			}
//			
//			if (memberService.isMyEmail(memberEmail, getCurrentId())) {
//				errors.rejectValue("memberEmail", "NotMyEmail");
//				return;
//			}
//			
//			if (memberService.isEmailExist(memberEmail)) {
//				errors.rejectValue("memberEmail", "EmailExist");
//				return;
//			}
//			
//			return;
//		}
		
//		if (type == Type.FIND_ACCOUNT) {
//			if (!StringUtils.hasText(memberEmail)) {
//				errors.rejectValue("memberEmail", "NotBlank");
//				return;
//			} 
//			
//			if (!Pattern.matches(EMAIL_REGEXP, memberEmail)) {
//				errors.rejectValue("memberEmail", "Pattern");
//				return;
//			}
//			
//			return;
//		} 
		
//		if (type == Type.BEFORE_FIND_PASSWORD) {
//			if (!StringUtils.hasText(memberId)) {
//				errors.rejectValue("memberId", "NotBlank");
//				return;
//			}
//			
//			if (!StringUtils.hasText(memberEmail)) {
//				errors.rejectValue("memberEmail", "NotBlank");
//				return;
//			} 
//			
//			if (!Pattern.matches(EMAIL_REGEXP, memberEmail)) {
//				errors.rejectValue("memberEmail", "Pattern");
//				return;
//			}
//			
//			if (!memberService.isAccountExist(memberId, memberEmail)) {
//				errors.rejectValue("memberId", "NotIdOrEmailExist");
//				errors.rejectValue("memberEmail", "NotIdOrEmailExist");
//				return;
//			}
//				
//			return;
//		}
		
	}
}
