package com.codingjoa.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BoardDetailsDto {

	private int boardIdx;
	private String boardTitle;
	private String boardContent;
	private int boardWriterIdx;
	private int boardViews;
	private int boardCategoryCode;
	
	@DateTimeFormat(pattern = "yyyy.MM.dd. HH:mm:ss")
	private Date regdate;
	
	@DateTimeFormat(pattern = "yyyy.MM.dd. HH:mm:ss")
	private Date moddate;
	
	private String memberId;		// member
	private int commentCnt;			// comment
	private int boardLikesCnt;		// board_likes
	
}
