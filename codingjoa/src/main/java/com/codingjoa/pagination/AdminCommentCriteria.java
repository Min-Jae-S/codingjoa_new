package com.codingjoa.pagination;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminCommentCriteria {

	private int page;
	private int recordCnt;
	
	public AdminCommentCriteria(int page, int recordCnt) {
		this.page = page;
		this.recordCnt = recordCnt;
	}
	
}
