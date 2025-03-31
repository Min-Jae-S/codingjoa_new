package com.codingjoa.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.AdminUserInfoDto;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class AdminUserInfoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return AdminUserInfoDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		AdminUserInfoDto adminUserInfoDto = (AdminUserInfoDto) target;
	}

}
