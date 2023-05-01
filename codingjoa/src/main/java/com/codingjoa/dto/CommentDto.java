package com.codingjoa.dto;

import com.codingjoa.converter.WhitespaceDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
public class CommentDto {

	private int commentIdx;
	private int commentWriterIdx;
	private int commentBoardIdx;
	private int boardCategoryCode;

	@JsonDeserialize(using = WhitespaceDeserializer.class)
	private String commentContent;
	
	private boolean commentUse;
	
}
