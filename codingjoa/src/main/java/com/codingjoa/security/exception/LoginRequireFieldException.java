package com.codingjoa.security.exception;

import org.springframework.security.core.AuthenticationException;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class LoginRequireFieldException extends AuthenticationException {

	public LoginRequireFieldException(String msg) {
		super(msg);
	}
}
