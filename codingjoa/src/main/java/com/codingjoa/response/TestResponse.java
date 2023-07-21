package com.codingjoa.response;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestResponse {
	
	private HttpStatus status;
	private String message;
	private List<FieldError> errors;
	
	@Builder.Default
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:ss:mm", timezone = "Asia/Seoul")
	private LocalDateTime timestamp = LocalDateTime.now();
}
