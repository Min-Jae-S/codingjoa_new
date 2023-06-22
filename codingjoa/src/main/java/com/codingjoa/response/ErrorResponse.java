package com.codingjoa.response;

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
	
	private String errorMessage;
	
	// key = errorField, value = errorMessage
	private Map<String, Object> errorMap; 
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:ss:mm", timezone = "Asia/Seoul")
	private LocalDateTime responseDateTime = LocalDateTime.now();
	
	public static ErrorResponse create() {
		return new ErrorResponse();
	}
	
	// simple message1 - LoginFailureHandler, CustomAuthenticationEntryPoint
	public ErrorResponse errorCode (String errorCode) {
		errorMessage = MessageUtils.getMessage(errorCode);
		return this;
	}
	
	// simple message2
	public ErrorResponse errorMessage(String message) {
		errorMessage = message;
		return this;
	}
	
	public ErrorResponse bindingResult(BindingResult bindingResult) {
		mapError(bindingResult);
		return this;
	}
	
	private void mapError(BindingResult bindingResult) {
		errorMap = new HashMap<>();
		
		bindingResult.getFieldErrors().forEach(fieldError -> {
			String errorField = fieldError.getField();
			String errorCode = fieldError.getCodes()[0];
			Object[] args = fieldError.getArguments();
			
			if (args != null) {
				errorMap.put(errorField, MessageUtils.getMessage(errorCode, args));
			} else {
				errorMap.put(errorField, MessageUtils.getMessage(errorCode));
			}
		});
	}
}
