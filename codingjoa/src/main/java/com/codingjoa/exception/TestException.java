package com.codingjoa.exception;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class TestException extends RuntimeException {
	
	private final String errorField;
	private final String errorCode;
	
	public TestException(String errorCode) {
		this.errorField = null;
		this.errorCode = errorCode;
	}

	public TestException(String errorField, String errorCode) {
		this.errorField = errorField;
		this.errorCode = errorCode;
	}
}
