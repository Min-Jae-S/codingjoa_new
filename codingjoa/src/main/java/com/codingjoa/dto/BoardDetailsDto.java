package com.codingjoa.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BoardDetailsDto {

	private int boardIdx;
	private String boardTitle;
	private String boardContent;
	private int boardWriterIdx;
	private int boardViews;
	private int boardCategoryCode;
	private Date regdate;
	private Date moddate;
	
	private String memberId;		// INNER JOIN with member
	private int commentCnt;			// OUTER JOIN with comment
	private int boardLikesCnt;		// OUTER JOIN with board_likes
	
	@Override
	public String toString() {
		String escapedBoardContent = boardContent.replace(System.lineSeparator(), "\\n");
		return "BoardDetailsDto [boardIdx=" + boardIdx + ", boardTitle=" + boardTitle + ", boardContent=" + escapedBoardContent
				+ ", boardWriterIdx=" + boardWriterIdx + ", boardViews=" + boardViews + ", boardCategoryCode="
				+ boardCategoryCode + ", regdate=" + regdate + ", moddate=" + moddate + ", memberId=" + memberId
				+ ", commentCnt=" + commentCnt + ", boardLikesCnt=" + boardLikesCnt + "]";
	}
	
}
