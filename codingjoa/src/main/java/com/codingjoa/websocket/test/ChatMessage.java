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
	public ChatMessage(ChatType type, String sender, String senderNickname, Object content) {
		this.type = type;
		this.sender = sender;
		this.senderNickname = senderNickname;
		this.content = content;
	}
	
}
