package com.codingjoa.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CommentDetailsDto {

	private int commentIdx;
	private int commentWriterIdx;
	private int commentBoardIdx;
	private String commentContent;
	private boolean commentUse;
	private Date regdate;
	private Date moddate;
	
	private String memberId;		// member
	private int commentLikes;		// comment_likes
	
}
