package com.codingjoa.websocket.test;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TestWebSocketReponse {

	private String id;
	private String from;
	private String content;
	private LocalDateTime timestamp = LocalDateTime.now();
	
	@Builder
	private TestWebSocketReponse(String id, String from, String content) {
		this.id = id;
		this.from = from;
		this.content = content;
	}
	
}
