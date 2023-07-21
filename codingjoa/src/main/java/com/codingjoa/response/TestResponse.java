package com.codingjoa.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class TestResponse {
	
	private HttpStatus status;
	private String message;
	private String code;
	private List<ErrorDetails> errors;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:ss:mm", timezone = "Asia/Seoul")
	private final LocalDateTime timestamp;
	
	private TestResponse() {
		this.errors = new ArrayList<ErrorDetails>();
		this.timestamp =  LocalDateTime.now();
	}
	
	public static TestResponseBuilder builder() {
		return new TestResponseBuilder();
	}

	public static class TestResponseBuilder {
		private TestResponse testResponse;
		private boolean codeMethodCalled;
		
		private TestResponseBuilder() {
			this.testResponse = new TestResponse();
			this.codeMethodCalled = false;
		}
		
		public TestResponseBuilder status(HttpStatus status) {
			testResponse.status = status;
			return this;
		}

		public TestResponseBuilder message(String message) {
			testResponse.message = message;
			return this;
		}

		public TestResponseBuilder code(String code) {
			if (!codeMethodCalled) {
				testResponse.code = code;
				testResponse.message = MessageUtils.getMessage(code);
				codeMethodCalled = true;
			}
			return this;
		}
		
		public TestResponseBuilder errors(List<ErrorDetails> errors) {
            testResponse.errors.addAll(errors);
            return this;
        }
		
		public TestResponse build() {
			return testResponse;
		}
	}
}
