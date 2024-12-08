package com.codingjoa.websocket;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ChatDto {

	private String sender;
	private String receiver;
	private String content;
	private LocalDateTime timestamp = LocalDateTime.now();

	@Builder
	private ChatDto(String sender, String receiver, String content) {
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
	}
	
}
