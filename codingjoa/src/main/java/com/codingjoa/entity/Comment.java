package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

/*
	comment_idx              NUMBER,
    comment_writer_idx       NUMBER                       NOT NULL,
	comment_board_idx        NUMBER                       NOT NULL,
	comment_content          VARCHAR2(2000)               NOT NULL,
    comment_use              CHAR(1)                      NOT NULL,
	regdate                  DATE                         NOT NULL,
    moddate                  DATE                         NOT NULL,
*/

@Data
public class Comment {
			
	private Integer commentIdx;
	private Integer commentWriterIdx;
	private Integer commentBoardIdx;
	private String commentContent;
	private Boolean commentUse;
	private Date regdate;
	private Date moddate;

	@Override
	public String toString() {
		String escapedCommentContent = (commentContent != null) ? commentContent.replace("\n", "\\n") : null;
		return "Comment [commentIdx=" + commentIdx + ", commentWriterIdx=" + commentWriterIdx + ", commentBoardIdx="
				+ commentBoardIdx + ", commentContent=" + escapedCommentContent + ", commentUse=" + commentUse
				+ ", regdate=" + regdate + ", moddate=" + moddate + "]";
	}
}
