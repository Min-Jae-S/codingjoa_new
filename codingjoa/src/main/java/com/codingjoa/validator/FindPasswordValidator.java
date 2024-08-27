package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.FindPasswordDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FindPasswordValidator implements Validator {
	
	private static final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

	@Override
	public boolean supports(Class<?> clazz) {
		return FindPasswordDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		FindPasswordDto findPasswordDto = (FindPasswordDto) target;
		String memberId = findPasswordDto.getMemberId();
		String memberEmail = findPasswordDto.getMemberEmail();
		
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
	}

}
