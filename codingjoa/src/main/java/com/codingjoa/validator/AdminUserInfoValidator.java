package com.codingjoa.validator;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.AdminUserInfoDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminUserInfoValidator implements Validator {
	
	private static final String EMAIL_REGEXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
	private static final String NICKNAME_REGEXP = "^([a-zA-Z가-힣0-9]{2,10})$";

	@Override
	public boolean supports(Class<?> clazz) {
		return AdminUserInfoDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		AdminUserInfoDto adminUserInfoDto = (AdminUserInfoDto) target;
		validateEmail(adminUserInfoDto, errors);
		validateNickname(adminUserInfoDto, errors);
		validateAddrs(adminUserInfoDto, errors);
	}
	
	private void validateEmail(AdminUserInfoDto adminUserInfoDto, Errors errors) {
		String email = adminUserInfoDto.getEmail();
		
		if (!StringUtils.hasText(email)) {
			errors.rejectValue("email", "NotBlank");
			return;
		}
		
		if (!Pattern.matches(EMAIL_REGEXP, email)) {
			errors.rejectValue("email", "Pattern");
			return;
		} 
	}
	
	private void validateNickname(AdminUserInfoDto adminUserInfoDto, Errors errors) {
		String nickname = adminUserInfoDto.getNickname();
		
		if (!StringUtils.hasText(nickname)) {
			errors.rejectValue("nickname", "NotBlank");
			return;
		}
		
		if (!Pattern.matches(NICKNAME_REGEXP, nickname)) {
			errors.rejectValue("nickname", "Pattern");
			return;
		} 
	}
	
	private void validateAddrs(AdminUserInfoDto adminUserInfoDto, Errors errors) {
		String zipcode = adminUserInfoDto.getZipcode();
		String addr = adminUserInfoDto.getAddr();
		String addrDetail = adminUserInfoDto.getAddrDetail();
		
		if (!StringUtils.hasText(zipcode)) {
			errors.rejectValue("zipcode", "NotBlank");
			return;
		}

		if (!StringUtils.hasText(addr)) {
			errors.rejectValue("addr", "NotBlank");
			return;
		}

		if (!StringUtils.hasText(addrDetail)) {
			errors.rejectValue("addrDetail", "NotBlank");
			return;
		}
		
	}
		

}
