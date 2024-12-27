package com.codingjoa.websocket.test;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class ChatMessage {
	
	public enum ChatType {
		ENTER, EXIT, TALK, PUSH;
		
		@JsonCreator
		public static ChatType from(String s) {
			return ChatType.valueOf(s.toUpperCase());
		}
	}
	
	private ChatType type;
	private String sender;
	private String senderSessionId;
	private Boolean sessionMatched;
	private Object content;
	private LocalDateTime timestamp = LocalDateTime.now();
	
	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSenderSessionId(String senderSessionId) {
		this.senderSessionId = senderSessionId;
	}
	
	public void setSessionMatched(Boolean sessionMatched) {
		this.sessionMatched = sessionMatched;
	}

	@Builder
	public ChatMessage(ChatType type, String sender, String senderSessionId, Boolean sessionMatched, Object content,
			LocalDateTime timestamp) {
		this.type = type;
		this.sender = sender;
		this.senderSessionId = senderSessionId;
		this.sessionMatched = sessionMatched;
		this.content = content;
		this.timestamp = timestamp;
	}
	
}
