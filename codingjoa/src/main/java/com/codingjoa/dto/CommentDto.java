package com.codingjoa.dto;

import lombok.Data;

@Data
public class CommentDto {

	private int replyIdx;
	private int replyWriterIdx;
	private int replyBoardIdx;
	private String replyContent;
	private boolean replyUse;
	
}
