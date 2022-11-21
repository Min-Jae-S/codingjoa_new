package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.JoinDto;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.RedisService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "joinValidator")
public class JoinValidator implements Validator {

	private final String ID_REGEXP = "^([a-z0-9]{6,12})$";
	//private final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$";
	private final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])(?=\\S+$).{8,16}$";
	private final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

	@Autowired
	private MemberService memberService;

	@Autowired
	private RedisService redisService;

	@Override
	public boolean supports(Class<?> clazz) {
		return JoinDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== JoinValidator ==============");

		JoinDto joinDto = (JoinDto) target;
		checkId(joinDto.getMemberId(), errors);
		checkPassword(joinDto.getMemberPassword(), joinDto.getConfirmPassword(), errors);
		checkEmailAuth(joinDto.getMemberEmail(), joinDto.getAuthCode(), errors);
	}

	private void checkId(String memberId, Errors errors) {
		if (!StringUtils.hasText(memberId)) {
			errors.rejectValue("memberId", "NotBlank");
			return;
		}
		
		if (!Pattern.matches(ID_REGEXP, memberId)) {
			errors.rejectValue("memberId", "Pattern");
			return;
		} 
		
		if (memberService.isIdExist(memberId)) {
			errors.rejectValue("memberId", "IdExist");
			return;
		}
	}

	private void checkPassword(String memberPassword, String confirmPassword, Errors errors) {
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

	private void checkEmailAuth(String memberEmail, String authCode, Errors errors) {
		if (!StringUtils.hasText(memberEmail)) {
			errors.rejectValue("memberEmail", "NotBlank");
			return;
		}
		
		if (!Pattern.matches(EMAIL_REGEXP, memberEmail)) {
			errors.rejectValue("memberEmail", "Pattern");
			return;
		} 
		
		if (memberService.isEmailExist(memberEmail)) {
			errors.rejectValue("memberEmail", "EmailExist");
			return;
		}
		
		if (!redisService.hasAuthCode(memberEmail)) {
			errors.reject("memberEmail", "NotAuthCodeExist");
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

}
