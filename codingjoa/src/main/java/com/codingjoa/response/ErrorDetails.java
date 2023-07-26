package com.codingjoa.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorDetails {
	
	private String code;
	private String field;
	private String message;
	
}
