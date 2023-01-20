package com.codingjoa.dto;

import java.util.List;

import lombok.Data;

@Data
public class BoardDto {
	
	private int boardIdx;
	private int boardCategoryCode;
	private String boardTitle;
	private String boardContent;
	private String boardContentText;
	private int boardWriterIdx;
	private List<Integer> uploadIdxList;
	
}
