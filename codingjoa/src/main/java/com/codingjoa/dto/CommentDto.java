package com.codingjoa.dto;

import org.apache.commons.text.StringEscapeUtils;

import com.codingjoa.converter.WhitespaceDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
public class CommentDto {

	private int commentIdx;
	private int commentWriterIdx;
	private int commentBoardIdx;

	@JsonDeserialize(using = WhitespaceDeserializer.class)
	private String commentContent;
	
	private boolean commentUse;

	@Override
	public String toString() {
		return "CommentDto [commentIdx=" + commentIdx + ", commentWriterIdx=" + commentWriterIdx + ", commentBoardIdx="
				+ commentBoardIdx + ", commentContent=" + StringEscapeUtils.escapeJava(commentContent) + ", commentUse="
				+ commentUse + "]";
	}
}
