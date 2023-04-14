package com.codingjoa.dto;

import java.util.List;

import com.codingjoa.annotation.BoardCategoryCode;

import lombok.Data;

@Data
public class BoardDto {
	
	private int boardIdx;
	private String boardTitle;
	private String boardContent;
	private String boardContentText;
	private int boardWriterIdx;
	
	//@BoardCategoryCode
	private int boardCategoryCode;
	
	private List<Integer> uploadIdxList;
	
}
