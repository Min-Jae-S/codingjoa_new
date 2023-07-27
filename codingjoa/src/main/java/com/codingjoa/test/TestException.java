package com.codingjoa.test;

import com.codingjoa.response.ErrorDetails;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class TestException extends RuntimeException {
	
	private final ErrorDetails errorDetails;
	
	public TestException(ErrorDetails errorDetails) {
		super();
		this.errorDetails = errorDetails;
	}

	public TestException(String message, ErrorDetails errorDetails) {
		super(message);
		this.errorDetails = errorDetails;
	}
	
}
