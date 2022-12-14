package com.codingjoa.enumclass;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.ToString;

@ToString
public enum Type {
	BEFORE_JOIN, 
	BEFORE_UPDATE_EMAIL,
	UPDATE_EMAIL,
	BEFORE_FIND_ACCOUNT,
	FIND_ACCOUNT,
	BEFORE_FIND_PASSWORD,
	FIND_PASSWORD,
	BEFORE_UPDATE_PASSWORD,
	UPDATE_PASSWORD,
	RESET_PASSWORD;
	
	@JsonCreator
	public static Type from(String s) {
		return Type.valueOf(s.toUpperCase());
	}
	
}
