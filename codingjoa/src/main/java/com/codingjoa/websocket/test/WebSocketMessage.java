package com.codingjoa.websocket.test;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class WebSocketMessage {
	
	private ChatType type;
	private String sender;
	private boolean sessionMatched;
	private Object content;
	private LocalDateTime timestamp = LocalDateTime.now();
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public void setSessionMatched(boolean sessionMatched) {
		this.sessionMatched = sessionMatched;
	}

	@Builder
	public WebSocketMessage(ChatType type, String sender, boolean sessionMatched, Object content, LocalDateTime timestamp) {
		this.type = type;
		this.sender = sender;
		this.sessionMatched = sessionMatched;
		this.content = content;
		this.timestamp = timestamp;
	}
	
}
