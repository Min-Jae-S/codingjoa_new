package com.codingjoa.websocket.test;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@JsonIgnore
	private String senderSessionId;
	private Object content;
	private LocalDateTime timestamp = LocalDateTime.now();
	
	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSenderSessionId(String senderSessionId) {
		this.senderSessionId = senderSessionId;
	}

	@Builder
	public ChatMessage(ChatType type, String sender, String senderSessionId, Object content) {
		this.type = type;
		this.sender = sender;
		this.senderSessionId = senderSessionId;
		this.content = content;
	}
	
}
