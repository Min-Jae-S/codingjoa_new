package com.codingjoa.dto;

import lombok.Data;

@Data
public class ReplyDto {

	private int replyIdx;

	public ReplyDto(Integer replyIdx) {
		this.replyIdx = replyIdx;
	}
	
}
