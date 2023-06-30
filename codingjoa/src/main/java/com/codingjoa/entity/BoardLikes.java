package com.codingjoa.entity;

import lombok.Data;

/*
	board_likes_idx     NUMBER,
	board_idx           NUMBER      NOT NULL,
	member_idx          NUMBER      NOT NULL,
	regdate             DATE        NOT NULL,
*/

@Data
public class BoardLikes {
	
	private Integer boardLikesIdx;
	private Integer boardIdx;
	private Integer memberIdx;
}
