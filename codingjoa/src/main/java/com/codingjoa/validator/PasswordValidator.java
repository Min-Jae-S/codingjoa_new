package com.codingjoa.validator;

import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.codingjoa.dto.PasswordDto;
import com.codingjoa.dto.SessionDto;
import com.codingjoa.enumclass.Type;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "passwordValidator")
public class PasswordValidator implements Validator {

	private final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])(?=\\S+$).{8,16}$";

	@Autowired
	private MemberService memberService;
	
	@Resource(name = "sessionDto")
	@Lazy
	private SessionDto sessionDto;

	@Override
	public boolean supports(Class<?> clazz) {
		return PasswordDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info("## {}", this.getClass().getSimpleName());
		
		PasswordDto passwordDto = (PasswordDto) target;
		Type type = passwordDto.getType();
		log.info("\t > type = {}", type);
		
//		if (type == null) {
//			errors.rejectValue("memberPassword", "NotValidAccess");
//			return;
//		}
		
		String memberPassword = passwordDto.getMemberPassword();
		String confirmPassword = passwordDto.getConfirmPassword();
		
		if (type == Type.BEFORE_UPDATE_PASSWORD) {
			if (!StringUtils.hasText(memberPassword)) {
				errors.rejectValue("memberPassword", "NotBlank");
				return;
			} 
			
			if (!memberService.isMyPassword(memberPassword, getCurrentId())) {
				errors.rejectValue("memberPassword", "BadCredentials");
				return;
			}
			
			return;
		}
		
		if (type == Type.UPDATE_PASSWORD) {
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

			if (memberService.isMyPassword(memberPassword, getCurrentId())) {
				errors.rejectValue("memberPassword", "NotSafe");
				return;
			}
			
			return;
		}
		
		if (type == Type.RESET_PASSWORD) {
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
			
			String memberId = (String) sessionDto.getFindPasswordResult().get("memberId");
			
			if (memberService.isMyPassword(memberPassword, memberId)) {
				errors.rejectValue("memberPassword", "NotSafe");
				return;
			}
			
			return;
		}
		
	}

	private String getCurrentId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null) return null;

		Object principal = auth.getPrincipal();
		String currentId = null;

		if (principal instanceof UserDetailsDto) {
			UserDetailsDto userDetailsDto = (UserDetailsDto) principal;
			currentId = userDetailsDto.getMember().getMemberId();
		} else if (principal instanceof String) {
			// principal = anonymousUser
			currentId = null;
		}

		return currentId;
	}
}
