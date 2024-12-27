package com.codingjoa.websocket.test;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ChatMessageMixIn {
	
	@JsonIgnore
    private String senderSessionId;
}
