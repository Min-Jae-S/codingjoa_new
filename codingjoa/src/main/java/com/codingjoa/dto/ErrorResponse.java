package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
	
	private int status = HttpStatus.BAD_REQUEST.value();
	private String message;
	private List<ErrorDetails> details = new ArrayList<>();
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime timestamp = LocalDateTime.now();
	
	public static ErrorResponseBuilder builder() {
		return new ErrorResponseBuilder();
	}

	@ToString
	public static class ErrorResponseBuilder {
		private ErrorResponse errorResponse;

		private ErrorResponseBuilder() {
			this.errorResponse = new ErrorResponse();
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
			errorResponse.message = MessageUtils.getMessage(code);
			return this;
		}
		
		public ErrorResponseBuilder details(ErrorDetails errorDetails) {
			errorResponse.details.add(errorDetails);
            return this;
        }

		public ErrorResponseBuilder details(List<ErrorDetails> errorDetails) {
			errorResponse.details.addAll(errorDetails);
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
			errorResponse.details.addAll(errorDetails);
			return this;
		}
		
		public ErrorResponse build() {
			return errorResponse;
		}
	}
}
