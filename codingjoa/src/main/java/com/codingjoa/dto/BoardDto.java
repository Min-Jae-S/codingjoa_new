package com.codingjoa.dto;

import java.util.ArrayList;
import java.util.List;

import com.codingjoa.annotation.BoardCategoryCode;
import com.codingjoa.entity.Board;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BoardDto {
	
	private int boardIdx;
	private int memberIdx;
	private String boardTitle;
	private String boardContent;
	private String boardContentText;
	
	@BoardCategoryCode
	private int boardCategoryCode;
	
	// cannot deserialize instance of `java.util.ArrayList<java.lang.Integer>` out of VALUE_STRING token;
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private List<Integer> boardImages = new ArrayList<>();

	@Override
	public String toString() {
		String escapedBoardContent = (boardContent != null) ? boardContent.replace("\r\n", "\\r\\n") : null;
		return "BoardDto [boardIdx=" + boardIdx + ", memberIdx=" + memberIdx + ", boardTitle=" + boardTitle
				+ ", boardContent=" + escapedBoardContent + ", boardContentText=" + boardContentText + ", boardCategoryCode="
				+ boardCategoryCode + ", boardImages=" + boardImages + "]";
	}
	
	public Board toEntity() {
		return Board.builder()
				.memberIdx(this.memberIdx)
				.boardTitle(this.boardTitle)
				.boardContent(this.boardContent)
				.boardContentText(this.boardContentText)
				.boardCategoryCode(this.boardCategoryCode)
				.build();
	}
	
}
