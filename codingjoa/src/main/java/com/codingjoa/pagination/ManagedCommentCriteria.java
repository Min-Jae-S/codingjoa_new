package com.codingjoa.pagination;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ManagedCommentCriteria {

	private int page;
	private int recordCnt;
	
	public ManagedCommentCriteria(int page, int recordCnt) {
		this.page = page;
		this.recordCnt = recordCnt;
	}
	
	public static ManagedCommentCriteria create() {
		return new ManagedCommentCriteria(1, 10);
	}
	
}
