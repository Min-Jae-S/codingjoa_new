package com.codingjoa.entity;

import lombok.Data;

@Data
public class Category {
	
	private Integer code;
	private Integer parentCode;
	private String name;
	private String path;
	
}
