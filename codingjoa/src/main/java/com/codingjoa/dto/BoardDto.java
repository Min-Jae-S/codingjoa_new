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
	
	@BoardCategoryCode
	private int boardCategoryCode;
	private List<Integer> boardImages;
	
//	@Override
//	public String toString() {
//		return "BoardDto [boardIdx=" + boardIdx + ", boardTitle=" + boardTitle + ", boardWriterIdx=" + boardWriterIdx
//				+ ", boardCategoryCode=" + boardCategoryCode + ", boardImages=" + boardImages + "]";
//	}
	
	@Override
	public String toString() {
		return "BoardDto [boardIdx=" + boardIdx + ", boardTitle=" + boardTitle + ", boardContent=" + boardContent
				+ ", boardContentText=" + boardContentText + ", boardWriterIdx=" + boardWriterIdx
				+ ", boardCategoryCode=" + boardCategoryCode + ", boardImages=" + boardImages + "]";
	}
}
