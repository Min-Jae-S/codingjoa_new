package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.codingjoa.util.MessageUtils;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
	
	private int status;
	private String message;
	private Object details;
	private LocalDateTime timestamp;
	
	private ErrorResponse(int status, String message, Object details, LocalDateTime timestamp) {
		this.status = status;
		this.message = message;
		this.details = details;
		this.timestamp = timestamp;
	}
	
	public static ErrorResponseBuilder builder() {
		return new ErrorResponseBuilder();
	}

	public ErrorResponseBuilder toBuilder() {
		return new ErrorResponseBuilder(this);
	}

	@ToString
	public static class ErrorResponseBuilder {
		private ErrorResponse errorResponse;

		private ErrorResponseBuilder() {
			//this.errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "", null, LocalDateTime.now());
			//this.errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), null, null, LocalDateTime.now());
			this.errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "error", null, LocalDateTime.now());
		}
		
		private ErrorResponseBuilder(ErrorResponse errorResponse) {
			this.errorResponse = new ErrorResponse(errorResponse.status, errorResponse.message,
					errorResponse.details, errorResponse.timestamp);
		}
		
		public ErrorResponseBuilder status(HttpStatus httpStatus) {
			errorResponse.status = httpStatus.value();
			return this;
		}

		public ErrorResponseBuilder status(int status) {
			errorResponse.status = status;
			return this;
		}

		public ErrorResponseBuilder message(String message) {
			errorResponse.message = message;
			return this;
		}
		
		public ErrorResponseBuilder messageByCode(String code) {
			try {
				errorResponse.message = MessageUtils.getMessage(code);
			} catch (NoSuchMessageException e) {
				log.info("## {}, message not found for code: {}", e.getClass().getSimpleName(), code);
			}
			
			return this;
		}
		
		public ErrorResponseBuilder details(ErrorDetails errorDetails) {
			errorResponse.details = errorDetails;
			return this;
        }

		public ErrorResponseBuilder details(List<ErrorDetails> errorDetails) {
			errorResponse.details = errorDetails;
			return this;
		}

		public ErrorResponseBuilder details(Object obj) {
			errorResponse.details = obj;
			return this;
		}
		
		public ErrorResponseBuilder bindingResult(BindingResult bindingResult) {
			List<ErrorDetails> errorDetails = bindingResult.getFieldErrors()
					.stream()
					.map(fieldError -> ErrorDetails.builder()
							.field(fieldError.getField())
							.messageByCode(fieldError.getCodes()[0], fieldError.getArguments())
							.build()
					)
					.collect(Collectors.toList());
			errorResponse.details = errorDetails;
			return this;
		}
		
		public ErrorResponse build() {
			return errorResponse;
		}
	}
}
