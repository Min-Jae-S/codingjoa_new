package com.codingjoa.dto;

import lombok.Getter;

@Getter
public class BoardCountsCorrection {
	
	private Long boardId;
	private int realCommentCount;
	private int realLikeCount;
	
	@Override
	public String toString() {
		return "boardId: " + boardId + ", realCommentCount: " + realCommentCount + ", realLikeCount: " + realLikeCount;
	}
	
}
