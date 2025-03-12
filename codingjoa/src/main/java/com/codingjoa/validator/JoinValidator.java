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
	private final UserService memberService;

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
		validatePassword(joinDto, errors);
	}
	
	private void validateEmailAuth(JoinDto joinDto, Errors errors) {
		String memberEmail = joinDto.getMemberEmail();
		String authCode = joinDto.getAuthCode();
		
		if (!StringUtils.hasText(memberEmail)) {
			errors.rejectValue("memberEmail", "NotBlank");
			return;
		}
		
		if (!Pattern.matches(EMAIL_REGEXP, memberEmail)) {
			errors.rejectValue("memberEmail", "Pattern");
			return;
		} 
		
		if (!redisService.hasKey(memberEmail)) {
			errors.rejectValue("memberEmail", "NotAuthCodeExist");
			return;
		}
		
		if (!StringUtils.hasText(authCode)) {
			errors.rejectValue("authCode", "NotBlank");
			return;
		} 
		
		if (!redisService.isAuthCodeValid(memberEmail, authCode)) {
			errors.rejectValue("authCode", "NotValid");
			return;
		}
	}
	
	private void validateNickname(JoinDto joinDto, Errors errors) {
		String memberNickname = joinDto.getMemberNickname();
		
		if (!StringUtils.hasText(memberNickname)) {
			errors.rejectValue("memberNickname", "NotBlank");
			return;
		}
		
		
		if (!Pattern.matches(NICKNAME_REGEXP, memberNickname)) {
			errors.rejectValue("memberNickname", "Pattern");
			return;
		} 
		
		if (memberService.isNicknameExist(memberNickname)) {
			errors.rejectValue("memberNickname", "NicknameExist");
			return;
		}
		
	}

	private void validatePassword(JoinDto joinDto, Errors errors) {
		String memberPassword = joinDto.getMemberPassword();
		String confirmPassword = joinDto.getConfirmPassword();
		
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
