package com.codingjoa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardLikeDto {
	
	private boolean liked;
	private int likeCount;
	
	public BoardLikeDto(boolean liked, int likeCount) {
		this.liked = liked;
		this.likeCount = likeCount;
	}
	
}
