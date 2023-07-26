package com.codingjoa.response;

import lombok.Data;

@Data
public class ErrorDetails {
	
	private String code;
	private String field;
	private String message;
	
	
	
}
