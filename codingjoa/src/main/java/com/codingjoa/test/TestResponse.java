package com.codingjoa.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.codingjoa.response.ErrorDetails;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class TestResponse {
	
	private Integer status;
	private String message;
	private List<ErrorDetails> errors;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:ss:mm", timezone = "Asia/Seoul")
	private LocalDateTime timestamp;
	
	private TestResponse() {
		this.errors = new ArrayList<ErrorDetails>();
		this.timestamp =  LocalDateTime.now();
	}
	
	public static TestResponseBuilder builder() {
		return new TestResponseBuilder();
	}

	@ToString
	public static class TestResponseBuilder {
		private TestResponse testResponse;

		private TestResponseBuilder() {
			this.testResponse = new TestResponse();
		}
		
		public TestResponseBuilder status(HttpStatus httpStatus) {
			testResponse.status = httpStatus.value();
			return this;
		}

		public TestResponseBuilder message(String message) {
			testResponse.message = message;
			return this;
		}
		
		public TestResponseBuilder messageByCode(String code) {
			testResponse.message = MessageUtils.getMessage(code);
			return this;
		}
		
		public TestResponseBuilder error(ErrorDetails errorDetails) {
            testResponse.errors.add(errorDetails);
            return this;
        }
		
		public TestResponseBuilder errors(List<ErrorDetails> errors) {
            testResponse.errors.addAll(errors);
            return this;
        }
		
		public TestResponseBuilder bindingResult(BindingResult bindingResult) {
			bindingResult.getFieldErrors().forEach(fieldError -> {
				String errorField = fieldError.getField();
				String errorCode = fieldError.getCodes()[0];
				String errorMessage = MessageUtils.getMessage(errorCode, fieldError.getArguments());
				ErrorDetails errorDetails = ErrorDetails.builder()
						.field(errorField)
						.code(errorCode)
						.message(errorMessage)
						.build();
				testResponse.errors.add(errorDetails);
			});
			return this;
		}
		
		public TestResponse build() {
			return testResponse;
		}
	}
}
