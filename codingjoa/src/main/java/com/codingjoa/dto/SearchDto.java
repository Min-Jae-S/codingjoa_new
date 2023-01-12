package com.codingjoa.dto;

import lombok.Data;

@Data
public class SearchDto {

	private String type;
	private String keyword;
	private int recordSize;
	
	public SearchDto() {
		this.type = "T";
		this.recordSize = 10;
	}
	
}
