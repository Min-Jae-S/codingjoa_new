package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.FindPasswordDto;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.RedisService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "findPasswordValidator")
public class FindPasswordValidator implements Validator {

	private final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

	@Autowired
	private MemberService memberService;

	@Autowired
	private RedisService redisService;

	@Override
	public boolean supports(Class<?> clazz) {
		return FindPasswordDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("============== FindPasswordValidator ==============");

		FindPasswordDto findPasswordDto = (FindPasswordDto) target;
		String memberId = findPasswordDto.getMemberId();
		
		
		
		String memberEmail = findPasswordDto.getMemberEmail();
		String authCode = findPasswordDto.getAuthCode();
		
		if (!StringUtils.hasText(memberEmail)) {
			errors.rejectValue("memberEmail", "NotBlank");
			return;
		} 
		
		if (!Pattern.matches(EMAIL_REGEXP, memberEmail)) {
			errors.rejectValue("memberEmail", "Pattern");
			return;
		}

	}

}
