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
	private List<ErrorDetails> errorDetails;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime timestamp;
	
	private TestResponse() {
		this.errorDetails = new ArrayList<ErrorDetails>();
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
		
		public TestResponseBuilder errorDetails(ErrorDetails errorDetails) {
            testResponse.errorDetails.add(errorDetails);
            return this;
        }

		public TestResponseBuilder errorDetails(List<ErrorDetails> errorDetails) {
			testResponse.errorDetails.addAll(errorDetails);
			return this;
		}
		
		public TestResponseBuilder bindingResult(BindingResult bindingResult) {
			bindingResult.getFieldErrors().forEach(fieldError -> {
				ErrorDetails errorDetails = ErrorDetails.builder()
						.field(fieldError.getField())
						.messageByCode(fieldError.getCodes()[0], fieldError.getArguments())
						.build();
				testResponse.errorDetails.add(errorDetails);
			});
			
			
			
			
			
			return this;
		}
		
		public TestResponse build() {
			return testResponse;
		}
	}
}
