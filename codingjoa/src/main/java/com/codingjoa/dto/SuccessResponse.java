package com.codingjoa.dto;

import java.time.LocalDateTime;

import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;

import com.codingjoa.util.MessageUtils;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse {
	
	private int status;
	private String message;
	private Object data;
	private LocalDateTime timestamp;
	
	private SuccessResponse(int status, String message, Object data, LocalDateTime timestamp) {
		this.status = status;
		this.message = message;
		this.data = data;
		this.timestamp = timestamp;
	}
	
	public static SuccessResponseBuilder builder() {
		return new SuccessResponseBuilder();
	}
	
	@ToString
	public static class SuccessResponseBuilder {
		private SuccessResponse successResponse;

		private SuccessResponseBuilder() {
			this.successResponse = new SuccessResponse(HttpStatus.OK.value(), "SUCCESS", null, LocalDateTime.now());
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
			try {
				successResponse.message = MessageUtils.getMessage(code);
			} catch (NoSuchMessageException e) {
				log.info("## {}, message not found for code: {}", e.getClass().getSimpleName(), code);
			}
			
			return this;
		}

		public SuccessResponseBuilder messageByCode(String code, Object... args) {
			try {
				successResponse.message = MessageUtils.getMessage(code, args);
			} catch (NoSuchMessageException e) {
				log.info("## {}, message not found for code: {}", e.getClass().getSimpleName(), code);
			}
			
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
