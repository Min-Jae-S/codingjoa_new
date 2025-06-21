package com.codingjoa.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardCacheCountDto {
	
	private long id;
	private int viewCount;
	private int commentCount;
	private int likeCount;

}
