package com.codingjoa.response;

import java.time.LocalDateTime;

import com.codingjoa.util.MessageUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class SuccessResponse {
	
	private Object data;
	private String message;
	private LocalDateTime responseDateTime = LocalDateTime.now();
	
	public static SuccessResponse create() {
		return new SuccessResponse();
	}
	
	public SuccessResponse data(Object data) {
		this.data = data;
		return this;
	}
	
	public SuccessResponse message(String code) {
		this.message = MessageUtils.getMessage(code);
		return this;
	}
}
