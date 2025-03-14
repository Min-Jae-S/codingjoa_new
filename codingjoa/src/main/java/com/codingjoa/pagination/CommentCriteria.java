package com.codingjoa.pagination;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class CommentCriteria {

	private int page;
	private int recordCnt;
	
	public CommentCriteria(int page, int recordCnt) {
		this.page = page;
		this.recordCnt = recordCnt;
	}
	
}
