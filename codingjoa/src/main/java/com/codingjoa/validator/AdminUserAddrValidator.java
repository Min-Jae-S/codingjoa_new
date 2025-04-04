package com.codingjoa.validator;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.AdminUserAddrDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminUserAddrValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return AdminUserAddrDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		AdminUserAddrDto adminUserAddrDto = (AdminUserAddrDto) target;
		
		if (!StringUtils.hasText(adminUserAddrDto.getZipcode())) {
			errors.rejectValue("zipcode", "NotBlank");
			return;
		}
		
		if (!StringUtils.hasText(adminUserAddrDto.getAddr())) {
			errors.rejectValue("addr", "NotBlank");
			return;
		}
		
		if (!StringUtils.hasText(adminUserAddrDto.getAddrDetail())) {
			errors.rejectValue("addrDetail", "NotBlank");
			return;
		}
	}

}
