package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
	comment_idx              NUMBER,
    comment_writer_idx       NUMBER                       NOT NULL,
	board_idx                NUMBER                       NOT NULL,
	comment_content          VARCHAR2(2000)               NOT NULL,
    comment_use              CHAR(1)                      NOT NULL,
	created_at               DATE                         NOT NULL,
    updated_at               DATE                         NOT NULL,
*/

@Getter
@NoArgsConstructor // for mybatis resultSet
public class Reply {
			
	private Integer commentIdx;
	private Integer commentWriterIdx;
	private Integer boardIdx;
	private String commentContent;
	private Boolean commentUse;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@Builder
	private Reply(Integer commentIdx, Integer commentWriterIdx, Integer boardIdx, String commentContent,
			Boolean commentUse, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.commentIdx = commentIdx;
		this.commentWriterIdx = commentWriterIdx;
		this.boardIdx = boardIdx;
		this.commentContent = commentContent;
		this.commentUse = commentUse;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	@Override
	public String toString() {
		String escapedCommentContent = (commentContent != null) ? commentContent.replace("\n", "\\n") : null;
		return "Comment [commentIdx=" + commentIdx + ", commentWriterIdx=" + commentWriterIdx + ", boardIdx=" + boardIdx
				+ ", commentContent=" + escapedCommentContent + ", commentUse=" + commentUse + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
}
