package com.codingjoa.enumclass;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.ToString;

@ToString
public enum Type {
	JOIN, 
	BEFORE_UPDATE,
	UPDATE,
	BEFORE_FIND_ACCOUNT,
	FIND_ACCOUNT,
	BEFORE_FIND_PASSWORD,
	FIND_PASSWORD;
	
	@JsonCreator
	public static Type from(String s) {
		return Type.valueOf(s.toUpperCase());
	}
	
}
