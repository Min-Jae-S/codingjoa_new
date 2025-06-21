package com.codingjoa.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentCacheCountDto {

	private long id;
	private int likeCount;
	
	public static CommentCacheCountDto of(long id, int likeCount) {
		return CommentCacheCountDto.builder()
				.id(id)
				.likeCount(likeCount)
				.build();
	}
	
}
