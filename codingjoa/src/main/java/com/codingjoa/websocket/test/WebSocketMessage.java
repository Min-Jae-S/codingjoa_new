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
	private Boolean sessionMatched;
	private Object content;
	private LocalDateTime timestamp = LocalDateTime.now();
	
	public void setSessionMatched(Boolean sessionMatched) {
		this.sessionMatched = sessionMatched;
	}

	@Builder(toBuilder = true)
	public WebSocketMessage(ChatType type, String sender, Boolean sessionMatched, Object content) {
		this.type = type;
		this.sender = sender;
		this.sessionMatched = sessionMatched;
		this.content = content;
	}
	
}
