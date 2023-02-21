package com.codingjoa.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ReplyDetailsDto {

	private int replyIdx;
	private int replyWriterIdx;
	private int replyBoardIdx;
	private String replyContent;
	private int replyLike;
	private boolean replyUse;
	private Date regdate;
	private Date moddate;
	private String memberId;
	
}
