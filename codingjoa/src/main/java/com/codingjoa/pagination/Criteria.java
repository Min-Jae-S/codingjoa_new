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
	
	public Criteria() {
		this(1, 10, null, null);
	}

	public Criteria(int page, int recordCnt, String keyword, String type) {
		this.page = page;
		this.recordCnt = recordCnt;
		this.keyword = keyword;
		this.type = type;
	}
}
