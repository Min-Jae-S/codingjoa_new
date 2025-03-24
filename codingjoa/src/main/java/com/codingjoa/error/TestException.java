package com.codingjoa.error;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class TestException extends RuntimeException {
	
	private final String errorMessage;
	
	public TestException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
}
