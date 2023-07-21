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
	private String code;
	private String message;
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
        private boolean messageByCode;
		
		private TestResponseBuilder() {
			this.testResponse = new TestResponse();
			this.codeMethodCalled = false;
			this.messageByCode = false;
		}
		
		public TestResponseBuilder status(HttpStatus status) {
			testResponse.status = status;
			return this;
		}

		public TestResponseBuilder code(String code) {
			testResponse.code = code;
			codeMethodCalled = true;
			return this;
		}
		
		public TestResponseBuilder messageByCode(boolean messageByCode) {
			if (!codeMethodCalled) {
				throw new IllegalStateException("## 제약조건에 위배된 호출 : code메서드 호출이 선행되어야 합니다.");
			}
			
			if (messageByCode) {
				testResponse.message = MessageUtils.getMessage(testResponse.code);
			}
			this.messageByCode = messageByCode;
			return this;
		}
		
		public TestResponseBuilder message(String message) {
			if (messageByCode) {
				throw new IllegalStateException("## 제약조건에 위배된 호출 : code에 의해 message가 이미 등록되었습니다.");
			}
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
