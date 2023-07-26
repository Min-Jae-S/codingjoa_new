package com.codingjoa.test;

import com.codingjoa.response.ErrorDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@AllArgsConstructor
@Getter
public class TestException extends RuntimeException {
	
	private final ErrorDetails errorDetails;
	
}
