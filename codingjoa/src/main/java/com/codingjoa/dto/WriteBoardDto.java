package com.codingjoa.dto;

import java.util.List;

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
	private List<Integer> uploadIdxList;
	
}
