package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

/*
	board_idx               NUMBER,
	board_title             VARCHAR2(500)               NOT NULL,
	board_content           LONG                        NOT NULL,
	board_file              VARCHAR2(500),
    board_writer_idx        NUMBER                      NOT NULL,
    board_view              NUMBER          DEFAULT 0   NOT NULL,
	board_category_code     NUMBER                      NOT NULL,
	regdate                 DATE                        NOT NULL,
    moddate                 DATE                        NOT NULL,
*/

@Data
public class Board {

	private Long boardIdx;
	private String boardTitle;
	private String boardContent;
	private String boardFile;
	private Long boardWriterIdx;
	private Long boardView;
	private Long boardCategoryCode;
	private Date regdate;
	private Date modddate;
	
}
