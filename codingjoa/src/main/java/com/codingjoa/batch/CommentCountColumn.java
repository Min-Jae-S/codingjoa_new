package com.codingjoa.batch;

import lombok.Getter;

@Getter
public class CommentCountColumn {
	
	private Long commentId;
	private int likeCount;
	private int realLikeCount;
	
	public boolean hasLikeCountMismatch() {
		return (likeCount != realLikeCount);
	}
	
}
