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
	private String senderNickname;
	private Object content;
	private LocalDateTime timestamp = LocalDateTime.now();
	
	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSenderNickname(String senderNickname) {
		this.senderNickname = senderNickname;
	}

	@Builder
	public Message(String type, String sender, String senderNickname, Object content) {
		this.type = type;
		this.sender = sender;
		this.senderNickname = senderNickname;
		this.content = content;
	}
	
}
