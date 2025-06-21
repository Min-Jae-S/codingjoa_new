package com.codingjoa.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardCacheCountDto {
	
	private long id;
	private int viewCount;
	private int commentCount;
	private int likeCount;
	
	public static BoardCacheCountDto of(long id, int viewCount, int commentCount, int likeCount) {
		return BoardCacheCountDto.builder()
				.id(id)
				.viewCount(viewCount)
				.commentCount(commentCount)
				.likeCount(likeCount)
				.build();
	}

}
