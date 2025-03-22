package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.PasswordResetDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordResetValidator implements Validator {

	private static final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])(?=\\S+$).{8,16}$";

	@Override
	public boolean supports(Class<?> clazz) {
		return PasswordResetDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		PasswordResetDto passwordResetDto = (PasswordResetDto) target;
		String newPassword = passwordResetDto.getNewPassword();
		String confirmPassword = passwordResetDto.getConfirmPassword();
		
		if (!StringUtils.hasText(newPassword)) {
			errors.rejectValue("newPassword", "NotBlank");
			return;
		} 

		if (!Pattern.matches(PASSWORD_REGEXP, newPassword)) {
			errors.rejectValue("newPassword", "Pattern");
			return;
		}

		if (!StringUtils.hasText(confirmPassword)) {
			errors.rejectValue("confirmPassword", "NotBlank");
			return;
		} 
		
		if (!newPassword.equals(confirmPassword)) {
			errors.rejectValue("newPassword", "NotEquals");
			errors.rejectValue("confirmPassword", "NotEquals");
			return;
		}
	}

}
