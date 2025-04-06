package com.codingjoa.pagination;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUserCriteria {
	
	private int page;
	private int recordCnt;
	private String keyword;
	private String type;
	
	public AdminUserCriteria(int page, int recordCnt, String keyword, String type) {
		this.page = page;
		this.recordCnt = recordCnt;
		this.keyword = keyword;
		this.type = type;
	}
	
}