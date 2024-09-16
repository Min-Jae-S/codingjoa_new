package com.codingjoa.dto;

import javax.validation.constraints.NotBlank;

import com.codingjoa.converter.WhitespaceDeserializer;
import com.codingjoa.entity.Comment;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentDto {

	private int commentIdx;
	private int memberIdx;
	private int boardIdx;

	@JsonDeserialize(using = WhitespaceDeserializer.class)
	@NotBlank
	private String commentContent;
	
	private boolean commentUse;
	
	@Builder
	private CommentDto(int commentIdx, int memberIdx, int boardIdx, @NotBlank String commentContent,
			boolean commentUse) {
		this.commentIdx = commentIdx;
		this.memberIdx = memberIdx;
		this.boardIdx = boardIdx;
		this.commentContent = commentContent;
		this.commentUse = commentUse;
	}
	
	@Override
	public String toString() {
		String escapedCommentContent = (commentContent != null) ? commentContent.replace("\n", "\\n") : null;
		return "CommentDto [commentIdx=" + commentIdx + ", memberIdx=" + memberIdx + ", boardIdx=" + boardIdx
				+ ", commentContent=" + escapedCommentContent + ", commentUse=" + commentUse + "]";
	}

	public Comment toEntity() {
		return Comment.builder()
				.build();
	}
	
}
