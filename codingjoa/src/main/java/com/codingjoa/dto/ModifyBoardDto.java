package com.codingjoa.dto;

import java.util.List;

import lombok.Data;

@Data
public class ModifyBoardDto {
	
	private int boardIdx;
	private int boardCategoryCode;
	private String boardTitle;
	private String boardContent;
	private int boardWriterIdx;
	private List<Integer> uploadIdxList;
	
}
