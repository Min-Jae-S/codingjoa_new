package com.codingjoa.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CommentDetailsDto {

	private int commentIdx;
	private int commentWriterIdx;
	private int commentBoardIdx;
	private String commentContent;
	private boolean commentUse;
	
	@JsonFormat(pattern = "yyyy.MM.dd. HH:mm")
	private Date regdate;
	
	@JsonFormat(pattern = "yyyy.MM.dd. HH:mm")
	private Date moddate;
	
	private String memberId;		// member
	private int commentLikes;		// comment_likes
	
}
