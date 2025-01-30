package com.codingjoa.entity;

import lombok.Getter;

@Getter
public class BoardInfo {
	
	private Board board;
	private Category category;
	private Member writer;
	private Integer commentCnt;
	private Integer boardLikesCnt;
	
	@Override
	public String toString() {
		return "BoardInfo [board=" + board + ", category=" + category + ", writer=" + writer + ", commentCnt="
				+ commentCnt + ", boardLikesCnt=" + boardLikesCnt + "]";
	}

}
