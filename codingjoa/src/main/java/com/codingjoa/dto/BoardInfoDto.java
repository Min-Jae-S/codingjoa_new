package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardInfoDto {

	private int boardIdx;
	private String boardTitle;
	private String boardContent;
	private int boardViews;
	private int boardCategoryCode;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime createdAt;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime updatedAt;
	
	private String boardWriterNickname;	// from INNER JOIN with member
	private int commentCnt;				// from LEFT OUTER JOIN with comment
	private int boardLikesCnt;			// from LEFT OUTER JOIN with board_likes
	
	@Builder
	private BoardInfoDto(int boardIdx, String boardTitle, String boardContent, int boardViews, int boardCategoryCode,
			LocalDateTime createdAt, LocalDateTime updatedAt, String boardWriterNickname, int commentCnt, int boardLikesCnt) {
		this.boardIdx = boardIdx;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardViews = boardViews;
		this.boardCategoryCode = boardCategoryCode;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.boardWriterNickname = boardWriterNickname;
		this.commentCnt = commentCnt;
		this.boardLikesCnt = boardLikesCnt;
	}
	
	@Override
	public String toString() {
		String escapedBoardContent = (boardContent != null) ? boardContent.replace("\r\n", "\\r\\n") : null;
		return "BoardInfoDto [boardIdx=" + boardIdx + ", boardTitle=" + boardTitle + ", boardContent=" + escapedBoardContent
				+ ", boardViews=" + boardViews + ", boardCategoryCode=" + boardCategoryCode + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", boardWriterNickname=" + boardWriterNickname + ", commentCnt="
				+ commentCnt + ", boardLikesCnt=" + boardLikesCnt + "]";
	}
	
	public static BoardInfoDto from(Map<String, Object> map) {
		return BoardInfoDto.builder()
				.boardIdx((int) map.get("boardIdx"))
				.boardTitle((String) map.get("boardTitle"))
				.boardContent((String) map.get("boardContent"))
				.boardViews((int) map.get("boardViews"))
				.boardCategoryCode((int) map.get("boardCategoryCode"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.boardWriterNickname((String) map.get("boardWriterNickname"))
				.commentCnt((int) map.get("commentCnt"))
				.boardLikesCnt((int) map.get("boardLikesCnt"))
				.build();
	}
	
}
