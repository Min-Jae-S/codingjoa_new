package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.service.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EmailAuthValidator implements Validator {
	
	private static final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
	private final RedisService redisService;

	@Override
	public boolean supports(Class<?> clazz) {
		return EmailAuthDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		EmailAuthDto emailAuthDto = (EmailAuthDto) target;
		String email = emailAuthDto.getEmail();
		String authCode = emailAuthDto.getAuthCode();
		
		if (!StringUtils.hasText(email)) {
			errors.rejectValue("email", "NotBlank");
			return;
		} 
		
		if (!Pattern.matches(EMAIL_REGEXP, email)) {
			errors.rejectValue("email", "Pattern");
			return;
		}
		
		if (!redisService.hasKey(email)) {
			errors.rejectValue("email", "NotAuthCodeExist");
			return;
		}
		
		if (!StringUtils.hasText(authCode)) {
			errors.rejectValue("authCode", "NotBlank");
			return;
		}
		
		if (!redisService.isAuthCodeValid(email, authCode)) {
			errors.rejectValue("authCode", "NotValid");
			return;
		}
	}

}
