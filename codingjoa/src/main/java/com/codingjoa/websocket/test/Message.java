package com.codingjoa.websocket.test;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class Message {
	
	private String type;
	private String sender;
	private Object content;
	private LocalDateTime timestamp = LocalDateTime.now();
	
	public void setSender(String sender) {
		this.sender = sender;
	}

	@Builder
	public Message(String type, String sender, Object content) {
		this.type = type;
		this.sender = sender;
		this.content = content;
	}
	
}
