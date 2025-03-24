package com.codingjoa.error;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ExpectedException extends RuntimeException {

	private final String errorCode;
	private final String errorField;
	
	public ExpectedException(String errorCode, String errorField) {
		this.errorCode = errorCode;
		this.errorField = errorField;
	}
	
	public ExpectedException(String errorCode) {
		this(errorCode, null);
	}
	
}
