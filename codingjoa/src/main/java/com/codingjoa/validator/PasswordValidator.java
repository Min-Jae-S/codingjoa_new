package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.PasswordDto;
import com.codingjoa.dto.UpdatePasswordDto;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "passwordValidator")
public class PasswordValidator implements Validator {

	private final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])(?=\\S+$).{8,16}$";

	@Autowired
	MemberService memberService;

	@Override
	public boolean supports(Class<?> clazz) {
		return PasswordDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== PasswordValidator ==============");

		String objectName = errors.getObjectName();

		if (objectName.equals("passwordDto")) {
			PasswordDto passwordDto = (PasswordDto) target;
			checkPassword(passwordDto.getMemberPassword(), errors);
		} else if (objectName.equals("updatePasswordDto")) {
			UpdatePasswordDto updatePasswordDto = (UpdatePasswordDto) target;
			checkPasswords(updatePasswordDto.getMemberPassword(), updatePasswordDto.getConfirmPassword(), errors);
		}
	}

	private void checkPassword(String memberPassword, Errors errors) {
		if (!StringUtils.hasText(memberPassword)) {
			errors.rejectValue("memberPassword", "NotBlank");
			return;
		} 
		
		if (!memberService.isMyPassword(memberPassword, getCurrentId())) {
			errors.rejectValue("memberPassword", "BadCredentials");
			return;
		}
	}

	private void checkPasswords(String memberPassword, String confirmPassword, Errors errors) {
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

		if (memberService.isMyPassword(memberPassword, getCurrentId())) {
			errors.rejectValue("memberPassword", "NotSafe");
			return;
		}
	}

	private String getCurrentId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null) return null;

		Object principal = auth.getPrincipal();
		String currentId = null;

		if (principal instanceof UserDetailsDto) {
			UserDetailsDto userDetailsDto = (UserDetailsDto) principal;
			currentId = userDetailsDto.getMember().getMemberId();
		} else if (principal instanceof String) {
			currentId = null; // principal = anonymousUser
		}

		return currentId;
	}
}
