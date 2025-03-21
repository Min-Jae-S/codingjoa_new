package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.EmailDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailValidator implements Validator {
	
	private static final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

	@Override
	public boolean supports(Class<?> clazz) {
		return EmailDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		EmailDto emailDto = (EmailDto) target;
		String email = emailDto.getEmail();
		
		if (!StringUtils.hasText(email)) {
			errors.rejectValue("email", "NotBlank");
			return;
		} 
		
		if (!Pattern.matches(EMAIL_REGEXP, email)) {
			errors.rejectValue("email", "Pattern");
			return;
		}
	}

}
