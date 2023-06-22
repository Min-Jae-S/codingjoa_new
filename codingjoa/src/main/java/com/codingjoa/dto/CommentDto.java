package com.codingjoa.dto;

import javax.validation.constraints.NotBlank;

import com.codingjoa.converter.WhitespaceDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
public class CommentDto {

	private int commentIdx;
	private int commentWriterIdx;
	private int commentBoardIdx;

	@JsonDeserialize(using = WhitespaceDeserializer.class)
	@NotBlank
	private String commentContent;
	
	private boolean commentUse;

	@Override
	public String toString() {
		String escapedCommentContent = commentContent != null ? commentContent.replace("\n", "\\n") : null;
		return "CommentDto [commentIdx=" + commentIdx + ", commentWriterIdx=" + commentWriterIdx + ", commentBoardIdx="
				+ commentBoardIdx + ", commentContent=" + escapedCommentContent + ", commentUse=" + commentUse + "]";
	}
}
