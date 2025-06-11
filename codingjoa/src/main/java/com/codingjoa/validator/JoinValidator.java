package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.JoinDto;
import com.codingjoa.service.UserService;
import com.codingjoa.service.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JoinValidator implements Validator {

	private static final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
	private static final String NICKNAME_REGEXP = "^([a-zA-Z가-힣0-9]{2,10})$";
	private static final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])(?=\\S+$).{8,16}$";
	private final RedisService redisService;
	private final UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return JoinDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		JoinDto joinDto = (JoinDto) target;
		validateEmailAuth(joinDto, errors);
		validateNickname(joinDto, errors);
		validatePasswords(joinDto, errors);
	}
	
	private void validateEmailAuth(JoinDto joinDto, Errors errors) {
		String email = joinDto.getEmail();
		String authCode = joinDto.getAuthCode();
		
		if (!StringUtils.hasText(email)) {
			errors.rejectValue("email", "NotBlank");
			return;
		}
		
		if (!Pattern.matches(EMAIL_REGEXP, email)) {
			errors.rejectValue("email", "Pattern");
			return;
		} 
		
		if (!redisService.hasKey(email)) {
			errors.rejectValue("email", "RequiredEmailAuth");
			return;
		}
		
		if (!StringUtils.hasText(authCode)) {
			errors.rejectValue("authCode", "NotBlank");
			return;
		} 
		
		String savedCode = (String) redisService.get(email);
		if (!authCode.equals(savedCode)) {
			errors.rejectValue("authCode", "NotValid");
			return;
		}
	}
	
	private void validateNickname(JoinDto joinDto, Errors errors) {
		String nickname = joinDto.getNickname();
		
		if (!StringUtils.hasText(nickname)) {
			errors.rejectValue("nickname", "NotBlank");
			return;
		}
		
		if (!Pattern.matches(NICKNAME_REGEXP, nickname)) {
			errors.rejectValue("nickname", "Pattern");
			return;
		} 
		
		if (userService.isNicknameExist(nickname)) {
			errors.rejectValue("nickname", "NicknameExists");
			return;
		}
	}

	private void validatePasswords(JoinDto joinDto, Errors errors) {
		String password = joinDto.getPassword();
		String confirmPassword = joinDto.getConfirmPassword();
		
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
