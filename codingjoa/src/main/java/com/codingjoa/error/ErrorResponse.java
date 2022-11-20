package com.codingjoa.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;

import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class ErrorResponse {
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime responseDateTime = LocalDateTime.now();
	
	private String errorMessage;
	private Map<String, Object> errorMap; // key = errorField, value = errorMessage
	
	public static ErrorResponse create() {
		return new ErrorResponse();
	}
	
	public ErrorResponse errorCode (String errorCode) {
		errorMessage = MessageUtils.getMessage(errorCode);
		return this;
	}
	
	public ErrorResponse bindingResult(BindingResult bindingResult) {
		mapError(bindingResult);
		return this;
	}
	
	public void mapError(BindingResult bindingResult) {
		errorMap = new HashMap<>();
		
		bindingResult.getFieldErrors().forEach(fieldError -> {
			String errorField = fieldError.getField();
			String errorCode = fieldError.getCodes()[0];
			
			errorMap.put(errorField, MessageUtils.getMessage(errorCode));
		});
	}
}
