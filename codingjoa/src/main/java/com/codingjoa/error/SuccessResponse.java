package com.codingjoa.error;

import java.time.LocalDateTime;

import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class SuccessResponse {
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime responseDateTime = LocalDateTime.now();
	
	private Object data;
	private String message;
	
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
