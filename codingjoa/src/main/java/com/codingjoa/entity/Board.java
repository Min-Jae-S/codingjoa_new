package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

/*
	board_idx               NUMBER,
    member_idx              NUMBER                      NOT NULL,
	board_title             VARCHAR2(500)               NOT NULL,
    board_content           CLOB                        NOT NULL,
    board_content_text      CLOB                        NULL, -- NOT NULL --> NULL
	board_category_code     NUMBER                      NOT NULL,
    board_views             NUMBER          DEFAULT 0   NOT NULL,
	created_at              DATE                        NOT NULL,
    updated_at              DATE                        NOT NULL,
*/

@Data
public class Board {

	private Integer boardIdx;
	private Integer memberIdx;
	private String boardTitle;
	private String boardContent;
	private String boardContentText;
	private Integer boardCategoryCode;
	private Integer boardViews;
	private Date createdAt;
	private Date updatedAt;
	
	@Override
	public String toString() {
		String escapedBoardContent = (boardContent != null) ? boardContent.replace("\r\n", "\\r\\n") : null;
		return "Board [boardIdx=" + boardIdx + ", memberIdx=" + memberIdx + ", boardTitle=" + boardTitle
				+ ", boardContent=" + escapedBoardContent + ", boardContentText=" + boardContentText + ", boardCategoryCode="
				+ boardCategoryCode + ", boardViews=" + boardViews + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
	
}
