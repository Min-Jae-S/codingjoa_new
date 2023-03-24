package com.codingjoa.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCriteria {

	private int boardIdx;
	private int page;
	private int recordCnt;
	
}
