package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.PasswordChangeDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordChangeValidator implements Validator {

	private static final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])(?=\\S+$).{8,16}$";

	@Override
	public boolean supports(Class<?> clazz) {
		return PasswordChangeDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		PasswordChangeDto passwordsDto = (PasswordChangeDto) target;
		String currentPassword = passwordsDto.getCurrentPassword();
		String newPassword = passwordsDto.getNewPassword();
		String confirmPassword = passwordsDto.getConfirmPassword();
		
		if (!StringUtils.hasText(currentPassword)) {
			errors.rejectValue("currentPassword", "NotBlank");
			return;
		} 

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
		
		if (!Pattern.matches(PASSWORD_REGEXP, confirmPassword)) {
			errors.rejectValue("confirmPassword", "Pattern");
			return;
		}

		if (!newPassword.equals(confirmPassword)) {
			errors.rejectValue("newPassword", "NotEquals");
			errors.rejectValue("confirmPassword", "NotEquals");
			return;
		}
	}

}
