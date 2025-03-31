package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.AdminUserAuthDto;
import com.codingjoa.dto.PasswordChangeDto;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class AdminUserAuthValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return AdminUserAuthDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		AdminUserAuthDto adminUserAuthDto = (AdminUserAuthDto) target;
	}

}
