package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.service.RedisService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class EmailAuthValidator implements Validator {
	
	private final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
	private RedisService redisService;

	@Override
	public boolean supports(Class<?> clazz) {
		return EmailAuthDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		EmailAuthDto emailAuthDto = (EmailAuthDto) target;
		String memberEmail = emailAuthDto.getMemberEmail();
		String authCode = emailAuthDto.getAuthCode();
		
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
