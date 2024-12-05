package com.codingjoa.websocket.test;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestWebSocketReponse {

	private String id;
	private String chatMessage;
	
}
