package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.PasswordChangeDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordChangeValidator implements Validator {

	private final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])(?=\\S+$).{8,16}$";

	@Override
	public boolean supports(Class<?> clazz) {
		return PasswordChangeDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		PasswordChangeDto passwordsDto = (PasswordChangeDto) target;
		String memberPassword = passwordsDto.getMemberPassword();
		String confirmPassword = passwordsDto.getConfirmPassword();

		if (!StringUtils.hasText(memberPassword)) {
			errors.rejectValue("memberPassword", "NotBlank");
		} else if (!Pattern.matches(PASSWORD_REGEXP, memberPassword)) {
			errors.rejectValue("memberPassword", "Pattern");
		}

		if (!StringUtils.hasText(confirmPassword)) {
			errors.rejectValue("confirmPassword", "NotBlank");
		} else if (!Pattern.matches(PASSWORD_REGEXP, confirmPassword)) {
			errors.rejectValue("confirmPassword", "Pattern");
		}

		if (errors.hasFieldErrors("memberPassword") || errors.hasFieldErrors("confirmPassword")) {
			return;
		}

		if (!memberPassword.equals(confirmPassword)) {
			errors.rejectValue("memberPassword", "NotEquals");
			errors.rejectValue("confirmPassword", "NotEquals");
			return;
		}
	}

}
