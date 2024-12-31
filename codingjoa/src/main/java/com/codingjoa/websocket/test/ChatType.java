package com.codingjoa.websocket.test;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ChatType {
	
	BROADCAST, ENTER, EXIT, TALK;
	
	@JsonCreator
	public static ChatType from(String s) {
		return ChatType.valueOf(s.toUpperCase());
	}
	
}
