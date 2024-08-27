package com.codingjoa.entity;

import lombok.Data;

@Data
public class Category {
	
	private Integer categoryCode;
	private Integer categoryParentCode;
	private String categoryName;
	private String categoryPath;
	
}
