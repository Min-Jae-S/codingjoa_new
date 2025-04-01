package com.codingjoa.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.AdminUserRegistrationDto;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class AdminUserRegistrationValidator implements Validator {

	private static final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])(?=\\S+$).{8,16}$";

	@Override
	public boolean supports(Class<?> clazz) {
		return AdminUserRegistrationDto.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		AdminUserRegistrationDto adminUserRegistrationDto = (AdminUserRegistrationDto) target;
	}

}
