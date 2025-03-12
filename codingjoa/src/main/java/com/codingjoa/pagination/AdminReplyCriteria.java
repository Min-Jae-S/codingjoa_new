package com.codingjoa.pagination;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminReplyCriteria {

	private int page;
	private int recordCnt;
	
	public AdminReplyCriteria(int page, int recordCnt) {
		this.page = page;
		this.recordCnt = recordCnt;
	}
	
	public static AdminReplyCriteria create() {
		return new AdminReplyCriteria(1, 10);
	}
	
}
