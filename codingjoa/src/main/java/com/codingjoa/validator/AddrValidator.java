package com.codingjoa.validator;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.AddrDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddrValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return AddrDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		AddrDto addrDto = (AddrDto) target;
		
		if (!StringUtils.hasText(addrDto.getZipcode())) {
			errors.rejectValue("zipcode", "NotBlank");
			return;
		}
		
		if (!StringUtils.hasText(addrDto.getAddr())) {
			errors.rejectValue("addr", "NotBlank");
			return;
		}
		
		if (!StringUtils.hasText(addrDto.getAddrDetail())) {
			errors.rejectValue("addrDetail", "NotBlank");
			return;
		}
	}

}
