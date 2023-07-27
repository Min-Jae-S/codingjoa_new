package com.codingjoa.exception;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ExpectedException extends RuntimeException {

	private final String field;
	private final String code;
	
	public ExpectedException(String code) {
		this.field = null;
		this.code = code;
	}
	
	public ExpectedException(String field, String code) {
		this.field = field;
		this.code = code;
	}
}
