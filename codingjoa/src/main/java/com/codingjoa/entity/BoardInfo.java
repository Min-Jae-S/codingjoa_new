package com.codingjoa.entity;

import lombok.Getter;

@Getter
public class BoardInfo {
	
	private Board board;
	private Category category;
	private Member writer;
	private Integer commentCnt;
	private Integer boardLikesCnt;

}
