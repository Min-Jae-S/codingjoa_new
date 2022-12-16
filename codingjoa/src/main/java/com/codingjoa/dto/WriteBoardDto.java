package com.codingjoa.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class WriteBoardDto {
	
	@NotBlank
	private String boardTitle;
	
	@NotBlank
	private String boardContent;
	
	private int boardWriterIdx;
	private int boardCategoryCode;
	
}
