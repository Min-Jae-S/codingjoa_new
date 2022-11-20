package com.codingjoa.security.exception;

import org.springframework.security.core.AuthenticationException;

import lombok.Getter;

@Getter
public class LoginRequireFieldException extends AuthenticationException {
	
	private static final long serialVersionUID = 2417338241376612871L;
	
	public LoginRequireFieldException(String msg) {
		super(msg);
	}
}
