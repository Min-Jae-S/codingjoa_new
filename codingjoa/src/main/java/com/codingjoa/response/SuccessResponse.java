package com.codingjoa.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse {
	
	private int status = HttpStatus.OK.value();
	private String message;
	private Object data;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime timestamp = LocalDateTime.now();
	
	public static SuccessResponseBuilder builder() {
		return new SuccessResponseBuilder();
	}
	
	@ToString
	public static class SuccessResponseBuilder {
		private SuccessResponse successResponse;

		private SuccessResponseBuilder() {
			this.successResponse = new SuccessResponse();
		}
		
		public SuccessResponseBuilder status(HttpStatus httpStatus) {
			successResponse.status = httpStatus.value();
			return this;
		}

		public SuccessResponseBuilder status(int status) {
			successResponse.status = status;
			return this;
		}
		
		public SuccessResponseBuilder message(String message) {
			successResponse.message = message;
			return this;
		}
		
		public SuccessResponseBuilder messageByCode(String code) {
			successResponse.message = MessageUtils.getMessage(code);
			return this;
		}

		public SuccessResponseBuilder data(Object data) {
			successResponse.data = data;
			return this;
		}
		
		public SuccessResponse build() {
			return successResponse;
		}
	}
	
}
