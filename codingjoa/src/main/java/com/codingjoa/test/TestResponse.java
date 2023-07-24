package com.codingjoa.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.codingjoa.annotation.CodeCallRequired;
import com.codingjoa.annotation.CheckMessageByCode;
import com.codingjoa.response.ErrorDetails;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class TestResponse {
	
	private HttpStatus status;
	private String code;
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
		private boolean isCodeMethodCalled;
        private boolean isMessageByCodeSet;

		private TestResponseBuilder() {
			this.testResponse = new TestResponse();
			this.isCodeMethodCalled = false;
			this.isMessageByCodeSet = false;
		}
		
		public TestResponseBuilder status(HttpStatus status) {
			testResponse.status = status;
			isCodeMethodCalled = true;
			return this;
		}

		public TestResponseBuilder code(String code) {
			testResponse.code = code;
			return this;
		}
		
		@CodeCallRequired
		public TestResponseBuilder messageByCode(boolean messageByCode) {
			if (messageByCode) {
				testResponse.message = MessageUtils.getMessage(testResponse.code);
				isMessageByCodeSet = true;
			}
			return this;
		}
		
		@CheckMessageByCode
		public TestResponseBuilder message(String message) {
			testResponse.message = message;
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
