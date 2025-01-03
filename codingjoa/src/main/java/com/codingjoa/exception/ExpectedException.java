package com.codingjoa.exception;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ExpectedException extends RuntimeException {

	private final String errorCode;
	private final String errorField;
	
	public ExpectedException(String errorField, String errorCode) {
		this.errorCode = errorCode;
		this.errorField = errorField;
	}
	
	public ExpectedException(String errorCode) {
		this(errorCode, null);
	}
	
}
