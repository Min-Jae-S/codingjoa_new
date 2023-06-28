package com.codingjoa.entity;

import lombok.Data;

/*
	comment_likes_idx                   NUMBER,
    comment_idx             NUMBER      NOT NULL,
	member_idx              NUMBER      NOT NULL,
*/

@Data
public class CommentLikes {
	
	private Integer commentLikesIdx;
	private Integer commentIdx;
	private Integer memberIdx;
}
