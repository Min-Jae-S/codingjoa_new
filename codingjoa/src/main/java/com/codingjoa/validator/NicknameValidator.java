package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.NicknameDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NicknameValidator implements Validator {
	
	private static final String NICKNAME_REGEXP = "^([a-zA-Z가-힣0-9]{2,10})$";

	@Override
	public boolean supports(Class<?> clazz) {
		return NicknameDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		NicknameDto nicknameDto = (NicknameDto) target;
		String nickname = nicknameDto.getNickname();
		
		if (!StringUtils.hasText(nickname)) {
			errors.rejectValue("nickname", "NotBlank");
			return;
		} 
		
		if (!Pattern.matches(NICKNAME_REGEXP, nickname)) {
			errors.rejectValue("nickname", "Pattern");
			return;
		}
	}

}
