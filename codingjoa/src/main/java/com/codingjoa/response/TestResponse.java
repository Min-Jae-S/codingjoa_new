package com.codingjoa.response;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestResponse {
	
	private HttpStatus status;
	private String message;
	private List<FieldError> errors;
	private LocalDateTime timestamp;
}
