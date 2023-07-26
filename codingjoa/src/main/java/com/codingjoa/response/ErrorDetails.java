package com.codingjoa.response;

import com.codingjoa.util.MessageUtils;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ErrorDetails {
	
	private String field;
	private String message;
	
	private ErrorDetails() {
		
	}
	
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
