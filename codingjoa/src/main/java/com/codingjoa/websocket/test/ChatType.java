package com.codingjoa.websocket.test;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ChatType {
	
	ENTER, EXIT, TALK, PUSH;
	
	@JsonCreator
	public static ChatType from(String s) {
		return ChatType.valueOf(s.toUpperCase());
	}
	
}
