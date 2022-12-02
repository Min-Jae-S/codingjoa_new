package com.codingjoa.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BoardDto {
	
	private Long boardIdx;
	private String boardTitle;
	private String boardContent;
	private String boardFile;
	private Long boardWriterIdx;
	private Long boardViews;
	private Long boardCategoryCode;
	private Date regdate;
	private Date modddate;
}
