package com.codingjoa.batch;

import lombok.Getter;

@Getter
public class CommentCountColumn {
	
	private Long commentId;
	private int likeCount;
	private int realLikeCount;
	
	public boolean hasLikeCountMismatch() {
		return likeCount != realLikeCount;
	}
	
	public String getMismatchDetails() {
		StringBuilder sb = new StringBuilder(String.format("commentId: %d", commentId));
		if (hasLikeCountMismatch()) {
			sb.append(String.format(", likeCount mismatch (current=%d, real=%d)", likeCount, realLikeCount));
		} else {
			sb.append(", likeCount match");
		}
		
		return sb.toString();
	}
	
}
