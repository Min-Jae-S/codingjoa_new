package com.codingjoa.response;

import java.time.LocalDateTime;

import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SuccessResponse {
	
	private Object data;
	private String message;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime responseDateTime;
	
	private SuccessResponse() {
		this.responseDateTime = LocalDateTime.now();
	}
	
	public static SuccessResponse create() {
		return new SuccessResponse();
	}
	
	public SuccessResponse data(Object data) {
		this.data = data;
		return this;
	}
	
	public SuccessResponse code(String code) {
		this.message = MessageUtils.getMessage(code);
		return this;
	}
	
	public SuccessResponse message(String message) {
		this.message = message;
		return this;
	}
}
