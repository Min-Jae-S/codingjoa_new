package com.codingjoa.dto;

import lombok.Data;

@Data
public class ReplyDto {

	private Integer replyIdx;

	public ReplyDto(Integer replyIdx) {
		this.replyIdx = replyIdx;
	}
	
}
