package com.codingjoa.validator;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.AdminUserAuthDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminUserAuthValidator implements Validator {

	private static final List<String> ROLES = List.of("ROLE_ADMIN");
	
	@Override
	public boolean supports(Class<?> clazz) {
		return AdminUserAuthDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		AdminUserAuthDto adminUserAuthDto = (AdminUserAuthDto) target;
		List<String> roles = adminUserAuthDto.getRoles();
		
		if (roles == null) {
			errors.rejectValue("roles", "NotNull");
		}
		
		if (roles.isEmpty()) {
			return;
		}
		
		for (String role : roles) {
			if (!ROLES.contains(role)) {
				errors.rejectValue("roles", "NotValid", new Object[] { role }, null);
				return;
			}
		}
	}

}
