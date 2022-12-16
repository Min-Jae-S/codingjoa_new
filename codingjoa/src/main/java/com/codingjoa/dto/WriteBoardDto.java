package com.codingjoa.dto;

import lombok.Data;

@Data
public class WriteBoardDto {
	
	private String boardTitle;
	private String boardContent;
	private int boardWriterIdx;
	private int boardCategoryCode;
	
}
