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
	private Map<String, Object> errorMap; // key = errorField, value = errorMessage
	
	@JsonFormat(pattern = "yyyy.MM.dd. HH:mm:ss")
	private LocalDateTime responseDateTime = LocalDateTime.now();
	
	public static ErrorResponse create() {
		return new ErrorResponse();
	}
	
	// simple message - LoginFailureHandler, CustomAuthenticationEntryPoint
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
			Object[] args = fieldError.getArguments();
			
			if (args != null) {
				errorMap.put(errorField, MessageUtils.getMessage(errorCode, args));
			} else {
				errorMap.put(errorField, MessageUtils.getMessage(errorCode));
			}
		});
	}
}
