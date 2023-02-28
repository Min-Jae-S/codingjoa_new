package com.codingjoa.dto;

import lombok.Data;

@Data
public class CommentDto {

	private int commentIdx;
	private int commentWriterIdx;
	private int commentBoardIdx;
	private int boardCategoryCode;
	private String commentContent;
	private boolean commentUse;
	
}
