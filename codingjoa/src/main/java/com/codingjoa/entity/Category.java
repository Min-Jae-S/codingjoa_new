package com.codingjoa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
	code            NUMBER,
	parent_code     NUMBER,
	name            VARCHAR2(50)       NOT NULL,
	path            VARCHAR2(1000)     NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class Category {
	
	private Integer code;
	private Integer parentCode;
	private String name;
	private String path;
	
}
