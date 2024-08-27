package com.codingjoa.dto;

import com.codingjoa.util.MessageUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorDetails {
	
	private String field;
	private String message;
	
	public static ErrorDetailsBuilder builder() {
		return new ErrorDetailsBuilder();
	}
	
	@ToString
	public static class ErrorDetailsBuilder {
		private ErrorDetails errorDetails;
		
		private ErrorDetailsBuilder() {
			this.errorDetails = new ErrorDetails();
		}
		
		public ErrorDetailsBuilder field(String field) {
			errorDetails.field = field;
			return this;
		}

		public ErrorDetailsBuilder messageByCode(String code) {
			errorDetails.message = MessageUtils.getMessage(code);
			return this;
		}

		public ErrorDetailsBuilder messageByCode(String code, Object[] args) {
			errorDetails.message = MessageUtils.getMessage(code, args);
			return this;
		}
		
		public ErrorDetails build() {
			return errorDetails;
		}
	}
}
