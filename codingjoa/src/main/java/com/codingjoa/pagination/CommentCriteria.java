package com.codingjoa.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CommentCriteria {

	private int page;
	private int recordCnt;
	
	public CommentCriteria() { }
	
	public CommentCriteria(int page, int recordCnt) {
		this.page = page;
		this.recordCnt = recordCnt;
	}

}
