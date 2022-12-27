package com.codingjoa.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BoardDetailsDto {

	private int boardIdx;
	private String boardTitle;
	private String boardContent;
	private int boardWriterIdx;
	private int boardViews;
	private int boardCategoryCode;
	private Date regdate;
	private Date moddate;
	private String memberId;
	
}
