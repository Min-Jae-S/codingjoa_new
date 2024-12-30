package com.codingjoa.websocket.test;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class StompMessage {
	
	private ChatType type;
	private String sender;
	private String senderSessionId;
	private Boolean sessionMatched;
	private Object content;
	private LocalDateTime timestamp;

	@Builder(toBuilder = true)
	public StompMessage(ChatType type, String sender, String senderSessionId, Boolean sessionMatched, Object content,
			LocalDateTime timestamp) {
		this.type = type;
		this.sender = sender;
		this.senderSessionId = senderSessionId;
		this.sessionMatched = sessionMatched;
		this.content = content;
		this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
	}
	
}
