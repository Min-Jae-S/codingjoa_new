package com.codingjoa.security.exception;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class NotFoundEntityException extends RuntimeException {

	private String errorCode;
	
	public NotFoundEntityException(String errorCode) {
		this.errorCode = errorCode;
	}

}
