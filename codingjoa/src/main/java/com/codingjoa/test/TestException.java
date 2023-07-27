package com.codingjoa.test;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class TestException extends RuntimeException {
	
	private final String code;
	private final String field;
	
	public TestException(String code) {
		this.code = code;
		this.field = null;
	}

	public TestException(String code, String field) {
		this.code = code;
		this.field = field;
	}
}
