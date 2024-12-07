package com.codingjoa.websocket.test;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TestWebSocketReponse {

	private String id;
	private String chatMessage;
	private LocalDateTime timestamp = LocalDateTime.now();
	
	@Builder
	private TestWebSocketReponse(String id, String chatMessage) {
		this.id = id;
		this.chatMessage = chatMessage;
	}
	
}
