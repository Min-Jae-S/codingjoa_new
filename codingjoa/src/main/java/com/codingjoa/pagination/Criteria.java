package com.codingjoa.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Criteria {
	
	private int page;
	private int recordCnt;
	private String keyword;
	private String type;
	
//	public Criteria() {
//		this(1, 10);
//	}
//	
//	public Criteria(int page, int recordCnt) {
//		this.page = page;
//		this.recordCnt = recordCnt;
//	}
	
	
}
