package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.AdminUserRegistrationDto;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class AdminUserRegistrationValidator implements Validator {

	private static final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
	private static final String NICKNAME_REGEXP = "^([a-zA-Z가-힣0-9]{2,10})$";
	private static final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])(?=\\S+$).{8,16}$";

	@Override
	public boolean supports(Class<?> clazz) {
		return AdminUserRegistrationDto.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		AdminUserRegistrationDto adminUserRegistrationDto = (AdminUserRegistrationDto) target;
		
		validateEmail(adminUserRegistrationDto, errors);
		validateNickname(adminUserRegistrationDto, errors);
		validatePasswords(adminUserRegistrationDto, errors);
	}
	
	private void validateEmail(AdminUserRegistrationDto adminUserRegistrationDto, Errors errors) {
		String email = adminUserRegistrationDto.getEmail();
		
		if (!StringUtils.hasText(email)) {
			errors.rejectValue("email", "NotBlank");
			return;
		}
		
		if (!Pattern.matches(EMAIL_REGEXP, email)) {
			errors.rejectValue("email", "Pattern");
			return;
		} 
	}
	
	private void validateNickname(AdminUserRegistrationDto adminUserRegistrationDto, Errors errors) {
		String nickname = adminUserRegistrationDto.getNickname();
		
		if (!StringUtils.hasText(nickname)) {
			errors.rejectValue("nickname", "NotBlank");
			return;
		}
		
		if (!Pattern.matches(NICKNAME_REGEXP, nickname)) {
			errors.rejectValue("nickname", "Pattern");
			return;
		} 
	}
	
	private void validatePasswords(AdminUserRegistrationDto adminUserRegistrationDto, Errors errors) {
		String password = adminUserRegistrationDto.getPassword();
		String confirmPassword = adminUserRegistrationDto.getConfirmPassword();
		
		if (!StringUtils.hasText(password)) {
			errors.rejectValue("password", "NotBlank");
		} else if (!Pattern.matches(PASSWORD_REGEXP, password)) {
			errors.rejectValue("password", "Pattern");
		}

		if (!StringUtils.hasText(confirmPassword)) {
			errors.rejectValue("confirmPassword", "NotBlank");
		} else if (!Pattern.matches(PASSWORD_REGEXP, confirmPassword)) {
			errors.rejectValue("confirmPassword", "Pattern");
		}
		
		if (errors.hasFieldErrors("password") || errors.hasFieldErrors("confirmPassword")) {
			return;
		}

		if (!password.equals(confirmPassword)) {
			errors.rejectValue("password", "NotEquals");
			errors.rejectValue("confirmPassword", "NotEquals");
			return;
		}
	}

}
