package com.codingjoa.websocket.test;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class StompMessageMixIn {
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String senderSessionId;
	
}
