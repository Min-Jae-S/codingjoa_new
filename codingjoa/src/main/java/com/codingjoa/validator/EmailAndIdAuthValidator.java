package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.EmailAndIdAuth;
import com.codingjoa.service.RedisService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class EmailAndIdAuthValidator implements Validator {

	private final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
	private RedisService redisService;

	@Override
	public boolean supports(Class<?> clazz) {
		return EmailAndIdAuth.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		EmailAndIdAuth emailAndIdAuth = (EmailAndIdAuth) target;
		String memberId = emailAndIdAuth.getMemberId();
		String memberEmail = emailAndIdAuth.getMemberEmail();
		String authCode = emailAndIdAuth.getAuthCode();
		
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
}
