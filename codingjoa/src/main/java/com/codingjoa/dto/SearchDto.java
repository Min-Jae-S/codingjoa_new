package com.codingjoa.dto;

import lombok.Data;

@Data
public class SearchDto {

	private String searchType;
	private String searchKeyword;
	private int recordPerPage;
	
	public SearchDto() {
		this.recordPerPage = 15;
	}
	
	
	
}
