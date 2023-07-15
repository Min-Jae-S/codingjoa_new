package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.UpdateEmailDto;
import com.codingjoa.service.RedisService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "updateEmailValidator")
public class UpdateEmailValidator implements Validator {
	
	private final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
	
	@Autowired
	private RedisService redisService;

	@Override
	public boolean supports(Class<?> clazz) {
		return UpdateEmailDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		UpdateEmailDto updateEmailDto = (UpdateEmailDto) target;
		String memberEmail = updateEmailDto.getMemberEmail();
		String authCode = updateEmailDto.getAuthCode();
		
		if (!StringUtils.hasText(memberEmail)) {
			errors.rejectValue("memberEmail", "NotBlank");
			return;
		} 
		
		if (!Pattern.matches(EMAIL_REGEXP, memberEmail)) {
			errors.rejectValue("memberEmail", "Pattern");
			return;
		}
		
		if (!StringUtils.hasText(authCode)) {
			errors.rejectValue("authCode", "NotBlank");
			return;
		}
		
//		if (!redisService.hasAuthCode(memberEmail)) {
//			errors.rejectValue("memberEmail", "NotAuthCodeExist");
//			return;
//		}
//		
//		if (!redisService.isAuthCodeValid(memberEmail, authCode)) {
//			errors.rejectValue("authCode", "NotValid");
//			return;
//		}
	}

}
