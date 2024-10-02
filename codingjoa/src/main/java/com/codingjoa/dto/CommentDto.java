package com.codingjoa.dto;

import javax.validation.constraints.NotBlank;

import com.codingjoa.converter.WhitespaceDeserializer;
import com.codingjoa.entity.Comment;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // for deserializing (JSON --> Object)
public class CommentDto {

	private int commentIdx;
	private int commentWriterIdx;
	private int boardIdx;

	@JsonDeserialize(using = WhitespaceDeserializer.class)
	@NotBlank
	private String commentContent;
	
	private boolean commentUse;
	
	@Builder
	private CommentDto(int commentIdx, int commentWriterIdx, int boardIdx, String commentContent, boolean commentUse) {
		this.commentIdx = commentIdx;
		this.commentWriterIdx = commentWriterIdx;
		this.boardIdx = boardIdx;
		this.commentContent = commentContent;
		this.commentUse = commentUse;
	}
	
	@Override
	public String toString() {
		String escapedCommentContent = (commentContent != null) ? commentContent.replace("\n", "\\n") : null;
		return "CommentDto [commentIdx=" + commentIdx + ", commentWriterIdx=" + commentWriterIdx + ", boardIdx="
				+ boardIdx + ", commentContent=" + escapedCommentContent + ", commentUse=" + commentUse + "]";
	}

	public Comment toEntity() {
		return Comment.builder()
				.commentIdx(this.commentIdx)
				.commentWriterIdx(this.commentWriterIdx)
				.boardIdx(this.boardIdx)
				.commentContent(this.commentContent)
				.commentUse(this.commentUse)
				.build();
	}
	
}
