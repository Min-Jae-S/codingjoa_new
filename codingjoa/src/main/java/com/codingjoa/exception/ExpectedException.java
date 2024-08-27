package com.codingjoa.exception;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ExpectedException extends RuntimeException {

	private final String errorField;
	private final String errorCode;
	
	public ExpectedException(String errorCode) {
		this.errorField = null;
		this.errorCode = errorCode;
	}
	
	public ExpectedException(String errorField, String errorCode) {
		this.errorField = errorField;
		this.errorCode = errorCode;
	}
}
