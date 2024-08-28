package com.codingjoa.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

	ROLE_USER("일반회원"),
	ROLE_ADMIN("관리자");
	
	String value;
}
