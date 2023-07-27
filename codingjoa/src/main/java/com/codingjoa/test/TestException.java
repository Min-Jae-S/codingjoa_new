package com.codingjoa.test;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class TestException extends RuntimeException {
	
	private final String field;
	private final String code;
	
	public TestException(String code) {
		this.field = null;
		this.code = code;
	}

	public TestException(String field, String code) {
		this.field = field;
		this.code = code;
	}
}
