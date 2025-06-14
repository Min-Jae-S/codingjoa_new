package com.codingjoa.batch;

import lombok.Getter;

@Getter
public class BoardCountColumn {
	
	private Long boardId;
	private int commentCount;
	private int likeCount;
	private int realCommentCount;
	private int realLikeCount;
	
	public boolean hasCommentCountMismatch() {
		return (commentCount != realCommentCount);
	}

	public boolean hasLikeCountMismatch() {
		return (likeCount != realLikeCount);
	}
	
}
