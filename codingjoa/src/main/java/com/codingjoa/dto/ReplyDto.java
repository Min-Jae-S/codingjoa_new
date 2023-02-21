package com.codingjoa.dto;

import lombok.Data;

@Data
public class ReplyDto {

	private int replyIdx;
	private int replyWriterIdx;
	private int replyBoardIdx;
	private String replyContent;
	
}
