package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
	board_idx               NUMBER,
    board_writer_idx        NUMBER                      NOT NULL,
	board_title             VARCHAR2(500)               NOT NULL,
    board_content           CLOB                        NOT NULL,
    board_content_text      CLOB                        NULL,
	board_category_code     NUMBER                      NOT NULL,
    board_view             	NUMBER          DEFAULT 0   NOT NULL,
    comment_cnt             NUMBER          DEFAULT 0   NOT NULL,
    likes_cnt             	NUMBER          DEFAULT 0   NOT NULL,
	created_at              DATE                        NOT NULL,
    updated_at              DATE                        NOT NULL,
*/

@Getter
@NoArgsConstructor // for mybatis resultSet
public class Board {

	private Integer boardIdx;
	private Integer boardWriterIdx;
	private String boardTitle;
	private String boardContent;
	private String boardContentText;
	private Integer boardCategoryCode;
	private Integer boardView;
	private Integer commentCnt;
	private Integer likesCnt;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@Builder
	private Board(Integer boardIdx, Integer boardWriterIdx, String boardTitle, String boardContent, String boardContentText,
			Integer boardCategoryCode, Integer boardView, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.boardIdx = boardIdx;
		this.boardWriterIdx = boardWriterIdx;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardContentText = boardContentText;
		this.boardCategoryCode = boardCategoryCode;
		this.boardView = boardView;
	}

	@Override
	public String toString() {
		String escapedBoardContent = (boardContent != null) ? boardContent.replace("\r\n", "\\r\\n") : null;
		return "Board [boardIdx=" + boardIdx + ", boardWriterIdx=" + boardWriterIdx + ", boardTitle=" + boardTitle
				+ ", boardContent=" + escapedBoardContent + ", boardContentText=" + boardContentText + ", boardCategoryCode="
				+ boardCategoryCode + ", boardView=" + boardView + ", commentCnt=" + commentCnt + ", likesCnt="
				+ likesCnt + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
}
