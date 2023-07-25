package com.codingjoa.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;

import com.codingjoa.response.ErrorDetails;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

		private TestResponseBuilder() {
			this.testResponse = new TestResponse();
		}
		
		public TestResponseBuilder status(HttpStatus status) {
			testResponse.status = status;
			return this;
		}

		public TestResponseBuilder code(String code) {
			testResponse.code = code;
			return this;
		}
		
		public TestResponseBuilder messageByCode(boolean messageByCode) {
			if (messageByCode) {
				try {
					message(MessageUtils.getMessage(testResponse.code));
				} catch(NoSuchMessageException e) {
					log.info("## messageByCode: No message found under code");
					return this;
				}
			}
			return this;
		}
		
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