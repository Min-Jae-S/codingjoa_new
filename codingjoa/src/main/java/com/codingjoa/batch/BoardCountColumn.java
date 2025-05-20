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
		return commentCount != realCommentCount;
	}

	public boolean hasLikeCountMismatch() {
		return likeCount != realLikeCount;
	}
	
	public String getMismatchDetails() {
		StringBuilder sb = new StringBuilder(String.format("boardId: %d", boardId));
		if (hasCommentCountMismatch()) {
			sb.append(String.format(", commentCount mismatch (current=%d, real=%d)", commentCount, realCommentCount));
		} else {
			sb.append(", commentCount match");
		}
		
		if (hasLikeCountMismatch()) {
			sb.append(String.format(", likeCount mismatch (current=%d, real=%d)", likeCount, realLikeCount));
		} else {
			sb.append(", likeCount match");
		}
		
		return sb.toString();
	}
	
}
