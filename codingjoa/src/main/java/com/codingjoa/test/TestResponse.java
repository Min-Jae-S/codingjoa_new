package com.codingjoa.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.codingjoa.dto.ErrorDetails;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class TestResponse {
	
	private HttpStatus status;
	private String message;
	private List<ErrorDetails> details;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime timestamp;
	
	private TestResponse() {
		this.details = new ArrayList<ErrorDetails>();
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
			testResponse.status = httpStatus;
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
		
		public TestResponseBuilder details(ErrorDetails errorDetails) {
            testResponse.details.add(errorDetails);
            return this;
        }

		public TestResponseBuilder details(List<ErrorDetails> errorDetails) {
			testResponse.details.addAll(errorDetails);
			return this;
		}
		
		public TestResponseBuilder bindingResult(BindingResult bindingResult) {
			List<ErrorDetails> errorDetails = bindingResult.getFieldErrors()
					.stream()
					.map(fieldError -> ErrorDetails.builder()
							.field(fieldError.getField())
							.messageByCode(fieldError.getCodes()[0], fieldError.getArguments())
							.build()
					)
					.collect(Collectors.toList());
			testResponse.details.addAll(errorDetails);
			return this;
		}
		
		public TestResponse build() {
			return testResponse;
		}
	}
}
