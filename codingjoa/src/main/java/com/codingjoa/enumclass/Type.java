package com.codingjoa.enumclass;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.ToString;

@ToString
public enum Type {
	FIND_PASSWORD;
	
	@JsonCreator
	public static Type from(String s) {
		return Type.valueOf(s.toUpperCase());
	}
	
}
