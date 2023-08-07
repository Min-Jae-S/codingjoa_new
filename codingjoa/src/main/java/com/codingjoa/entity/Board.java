package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

/*
	board_idx               NUMBER,
	board_title             VARCHAR2(500)               NOT NULL,
	board_content           CLOB                        NOT NULL,
	board_content_text      CLOB                        NOT NULL,
    board_writer_idx        NUMBER                      NOT NULL,
    board_views             NUMBER          DEFAULT 0   NOT NULL,
	board_category_code     NUMBER                      NOT NULL,
	regdate                 DATE                        NOT NULL,
    moddate                 DATE                        NOT NULL,
*/

@Data
public class Board {

	private Integer boardIdx;
	private String boardTitle;
	private String boardContent;
	private String boardContentText;
	private Integer boardWriterIdx;
	private Integer boardViews;
	private Integer boardCategoryCode;
	private Date regdate;
	private Date moddate;
	
	@Override
	public String toString() {
		String escapedBoardContent = (boardContent != null) ? boardContent.replace("\r\n", "\\r\\n") : null;
		String escapedBoardContentText = (boardContentText != null) ? boardContentText.replace("\r\n", "\\r\\n") : null;
		return "Board [boardIdx=" + boardIdx + ", boardTitle=" + boardTitle + ", boardContent=" + escapedBoardContent
				+ ", boardContentText=" + escapedBoardContentText + ", boardWriterIdx=" + boardWriterIdx + ", boardViews="
				+ boardViews + ", boardCategoryCode=" + boardCategoryCode + ", regdate=" + regdate + ", moddate="
				+ moddate + "]";
	}
	
}
