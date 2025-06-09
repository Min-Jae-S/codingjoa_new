package com.codingjoa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentLikeDto {

	private boolean liked;
	private int likeCount;
	
	public CommentLikeDto(boolean liked, int likeCount) {
		this.liked = liked;
		this.likeCount = likeCount;
	}
	
}
