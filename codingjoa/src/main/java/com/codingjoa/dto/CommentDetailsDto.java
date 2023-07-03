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
	
	@JsonFormat(pattern = "yyyy.MM.dd. HH:mm", timezone = "Asia/Seoul")
	private Date regdate;
	
	@JsonFormat(pattern = "yyyy.MM.dd. HH:mm", timezone = "Asia/Seoul")
	private Date moddate;
	
	private String commentWriterId;	// member
	private int commentLikesCnt;	// comment_likes
	
	@Override
	public String toString() {
		String escapedCommentContent = commentContent != null ? commentContent.replace("\n", "\\n") : null;
		return "CommentDetailsDto [commentIdx=" + commentIdx + ", commentWriterIdx=" + commentWriterIdx
				+ ", commentBoardIdx=" + commentBoardIdx + ", commentContent=" + escapedCommentContent + ", regdate="
				+ regdate + ", moddate=" + moddate + ", commentWriterId=" + commentWriterId + ", commentLikes="
				+ commentLikesCnt + "]";
	}
}
