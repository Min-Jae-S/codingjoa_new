package com.codingjoa.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CommentCriteria {

	private int boardIdx;
	private int page;
	private int recordCnt;
	
	public CommentCriteria() { }
	
	public CommentCriteria(int boardIdx, int page, int recordCnt) {
		this.boardIdx = boardIdx;
		this.page = page;
		this.recordCnt = recordCnt;
	}

}
