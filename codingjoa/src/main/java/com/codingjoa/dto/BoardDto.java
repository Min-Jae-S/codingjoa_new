package com.codingjoa.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class BoardDto {
	
	private int boardIdx;
	
	@NotBlank
	private String boardTitle;
	
	private String boardContent;
	private String boardFile;
	private int boardWriterIdx;
	private int boardViews;
	private int boardCategoryCode;
	private Date regdate;
	private Date modddate;
}
