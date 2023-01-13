package com.codingjoa.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Criteria {
	
	private int page;
	private int recordSize;
	private String keyword;
	private String type;
	
	public Criteria() {
		this(1, 10);
	}
	
	public Criteria(int page, int recordSize) {
		this.page = page;
		this.recordSize = recordSize;
	}
	
	
}
