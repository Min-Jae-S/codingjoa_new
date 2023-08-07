package com.codingjoa.dto;

import java.util.ArrayList;
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
	private List<Integer> boardImages = new ArrayList<>();
	
	@Override
	public String toString() {
		String escapedBoardContent = (boardContent != null) ? boardContent.replace("\r\n", "\\r\\n") : null;
		String escapedBoardContentText = (boardContentText != null) ? boardContentText.replace("\r\n", "\\r\\n") : null;
		return "BoardDto [boardIdx=" + boardIdx + ", boardTitle=" + boardTitle + ", boardContent=" + escapedBoardContent
				+ ", boardContentText=" + escapedBoardContentText + ", boardWriterIdx=" + boardWriterIdx
				+ ", boardCategoryCode=" + boardCategoryCode + ", boardImages=" + boardImages + "]";
	}
}
