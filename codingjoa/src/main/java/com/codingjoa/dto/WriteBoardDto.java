package com.codingjoa.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class WriteBoardDto {
	
	@NotBlank
	private String boardTitle;
	
	@NotBlank
	private String boardContent;
	
	private String boardFile;
	private int boardWriterIdx;
	private int boardViews;
	private int boardCategoryCode;
	private Date regdate;
	private Date modddate;
}
