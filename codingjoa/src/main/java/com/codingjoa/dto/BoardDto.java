package com.codingjoa.dto;

import java.util.List;

import com.codingjoa.annotation.BoardCategoryCode;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class BoardDto {
	
	private int boardIdx;
	private String boardTitle;
	private String boardContent;
	private String boardContentText;
	private int boardWriterIdx;
	
	@BoardCategoryCode
	private int boardCategoryCode;
	private List<Integer> uploadIdxList;
	
	public BoardDto() {
		log.info("## BoardDto Constructor");
	}

	@Override
	public String toString() {
		return "BoardDto [boardIdx=" + boardIdx + ", boardTitle=" + boardTitle + ", boardWriterIdx=" + boardWriterIdx
				+ ", boardCategoryCode=" + boardCategoryCode + ", uploadIdxList=" + uploadIdxList + "]";
	}
	
}
