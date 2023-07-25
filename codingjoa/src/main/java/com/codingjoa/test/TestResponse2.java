package com.codingjoa.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.codingjoa.response.ErrorDetails;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class TestResponse2 {
	
	private HttpStatus status;
	private String code;
	private String message;
	private List<ErrorDetails> errors;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:ss:mm", timezone = "Asia/Seoul")
	private LocalDateTime timestamp;
	
	private TestResponse2() {
		this.errors = new ArrayList<ErrorDetails>();
		this.timestamp =  LocalDateTime.now();
	}
	
	public static TestResponseBuilder builder() {
		return new TestResponseBuilder();
	}

	@ToString
	public static class TestResponseBuilder {
		private TestResponse2 testResponse;
		private boolean isCodeMethodCalled;
        private boolean isMessageByCodeSet;
		
		private TestResponseBuilder() {
			this.testResponse = new TestResponse2();
			this.isCodeMethodCalled = false;
			this.isMessageByCodeSet = false;
		}
		
		public TestResponseBuilder status(HttpStatus status) {
			testResponse.status = status;
			return this;
		}

		public TestResponseBuilder code(String code) {
			testResponse.code = code;
			isCodeMethodCalled = true;
			return this;
		}
		
		public TestResponseBuilder messageByCode(boolean messageByCode) {
			if (!isCodeMethodCalled) {
				throw new IllegalStateException("## 제약조건에 위배된 호출 : code메서드 호출이 선행되어야 합니다.");
			}
			
			if (messageByCode) {
				testResponse.message = MessageUtils.getMessage(testResponse.code);
				this.isMessageByCodeSet = true;
			}
			return this;
		}
		
		public TestResponseBuilder message(String message) {
			if (isMessageByCodeSet) {
				throw new IllegalStateException("## 제약조건에 위배된 호출 : code에 의해 message가 이미 등록되었습니다.");
			}
			testResponse.message = message;
			return this;
		}
		
		public TestResponseBuilder errors(List<ErrorDetails> errors) {
            testResponse.errors.addAll(errors);
            return this;
        }
		
		public TestResponse2 build() {
			return testResponse;
		}
	}
}
