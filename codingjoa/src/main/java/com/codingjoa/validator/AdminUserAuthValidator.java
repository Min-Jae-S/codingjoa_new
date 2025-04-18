package com.codingjoa.validator;

import java.util.Set;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.AdminUserAuthDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminUserAuthValidator implements Validator {

	private static final Set<String> ALLOWED_ROLES = Set.of("ROLE_USER", "ROLE_ADMIN");
	
	@Override
	public boolean supports(Class<?> clazz) {
		return AdminUserAuthDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());

		AdminUserAuthDto adminUserAuthDto = (AdminUserAuthDto) target;
		Set<String> roles = adminUserAuthDto.getRoles();
		
		// "ROLE_USER"는 역직렬화 단계에서 기본값으로 포함
		// 따라서 roles는 항상 NotNull, NotEmpty
		
//		if (roles == null) {
//			errors.rejectValue("roles", "NotNull");
//		}
//		
//		if (roles.isEmpty()) {
//			return;
//		}
		
		for (String role : roles) {
			if (!ALLOWED_ROLES.contains(role)) {
				errors.rejectValue("roles", "NotValid", new Object[] { role }, null);
				return;
			}
		}
	}
}
